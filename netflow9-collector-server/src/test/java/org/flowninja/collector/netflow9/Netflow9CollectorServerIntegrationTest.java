
package org.flowninja.collector.netflow9;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.NetworkServerStartupMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collector.common.netflow9.actors.TemplateDecodingFailureMessage;
import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.collector.netflow9.actors.support.SingleMessageTimedSinkActor;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;
import org.flowninja.common.types.Header;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = Netflow9CollectorServerIntegrationTest.TestConfiguration.class)
@Slf4j
public class Netflow9CollectorServerIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private CompletionHolder completions;

    @Autowired
    private Netflow9CollectorProperties netflow9CollectorProperties;

    @After
    public void resetCompletions() {
        completions.reset();
    }

    @Test
    @SuppressWarnings("checkstyle:IllegalCatch")
    public void shouldDecodeDataFlowWhenTemplateAndFlowInOneDatagram() throws Exception {
        completions.getServerEventCompletion().whenComplete((m, t) -> {
            if (m.isPresent()) {
                log.info("Sending datagram to {}", netflow9CollectorProperties);

                try {
                    final Bootstrap b = new Bootstrap();

                    b.group(new NioEventLoopGroup())
                            .channel(NioDatagramChannel.class)
                            .handler(new ChannelInitializer<Channel>() {

                                @Override
                                protected void initChannel(final Channel ch) throws Exception {
                                    ch.pipeline().addLast(new ByteArrayEncoder());
                                }
                            });

                    final DatagramChannel c = (DatagramChannel) b
                            .bind(new InetSocketAddress(netflow9CollectorProperties.getAddress(), 0)).sync().channel();

                    c.connect(
                            new InetSocketAddress(
                                    netflow9CollectorProperties.getAddress(),
                                    netflow9CollectorProperties.getPort()))
                            .sync();
                    c.writeAndFlush(dataTemplateOneDataFlowDatagram).sync();

                } catch (Exception e) {
                    log.info("failed to send datagram", e);

                    throw new RuntimeException(e);
                }
            }
        });

        final Optional<DataFlowMessage> m = completions.getDataFlowCompletion().get();

        assertThat(m).isPresent();

        assertThat(m.get().getDataFlow()).isNotNull();
        assertThat(m.get().getDataFlow().getHeader()).isEqualTo(
                Header.builder()
                        .recordCount(2)
                        .sequenceNumber(31)
                        .sourceId(0)
                        .sysUpTime(1976904)
                        .unixSeconds(1427139251L)
                        .build());

        assertThat(m.get().getDataFlow().getUuid()).isNotNull();
        assertThat(m.get().getDataFlow().getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(m.get().getDataFlow().getRecords()).hasSize(21);
    }

    @Getter
    public static class CompletionHolder implements InitializingBean {

        private CompletableFuture<Optional<DataFlowMessage>> dataFlowCompletion;
        private CompletableFuture<Optional<OptionsFlowMessage>> optionsFlowCompletion;
        private CompletableFuture<Optional<TemplateDecodingFailureMessage>> decodingFailureCompletion;
        private CompletableFuture<Optional<NetworkServerStartupMessage>> serverEventCompletion;

        public void reset() {
            dataFlowCompletion = new CompletableFuture<>();
            optionsFlowCompletion = new CompletableFuture<>();
            decodingFailureCompletion = new CompletableFuture<>();
            serverEventCompletion = new CompletableFuture<>();
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            reset();
        }
    }

    @TestConfig
    @Import({ Netflow9CollectorConfiguration.class, AkkaConfiguration.class })
    public static class TestConfiguration {

        @Bean
        public Netflow9CollectorProperties netflow9CollectorProperties() throws Exception {
            final Netflow9CollectorProperties props = new Netflow9CollectorProperties();

            props.setAddress(InetAddress.getLoopbackAddress());
            props.setPort(SocketUtils.findAvailableUdpPort(49153));

            return props;
        }

        @Bean
        public CompletionHolder completionHolder() {
            return new CompletionHolder();
        }

        @Bean
        @Autowired
        @Scope("prototype")
        public SinkActorsProvider sinkActorsProvider(
                final SpringActorProducer springActorProducer,
                final CompletionHolder completionHolder) {
            return SinkActorsProvider.builder()
                    .dataFlowActor(
                            springActorProducer.createActor(
                                    SingleMessageTimedSinkActor.class,
                                    DataFlowMessage.class,
                                    completionHolder.getDataFlowCompletion(),
                                    5))
                    .optionsFlowActor(
                            springActorProducer.createActor(
                                    SingleMessageTimedSinkActor.class,
                                    OptionsFlowMessage.class,
                                    completionHolder.getOptionsFlowCompletion(),
                                    5))
                    .decodingFailureActor(
                            springActorProducer.createActor(
                                    SingleMessageTimedSinkActor.class,
                                    TemplateDecodingFailureMessage.class,
                                    completionHolder.getDecodingFailureCompletion(),
                                    5))
                    .serverEventActor(
                            springActorProducer.createActor(
                                    SingleMessageTimedSinkActor.class,
                                    NetworkServerStartupMessage.class,
                                    completionHolder.getServerEventCompletion(),
                                    5))
                    .build();
        }
    }

    private static final byte[] dataTemplateOneDataFlowDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x02, // record count: 2
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2

            0x01,
            0x00, // Flowset ID: 256
            0x00,
            0x34, // Flowset length: 52,
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // Last switched
            0x00,
            0x00,
            0x00,
            0x43, // Bytes: 67
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x04, // Input SNMP
            0x00,
            0x05, // Output SNMP
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP SRC Addr 192.168.4.10
            (byte) 0xc0,
            0x2b,
            (byte) 0xac,
            0x1e, // IP DST Addr 192.43.172.30
            0x11, // Protocol UDP
            0x00, // IP TOS
            (byte) 0xb3,
            (byte) 0xd5, // L4 SRC Port 46037
            0x00,
            0x35, // L4 DST Port 53
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x04, // Next hop 192.168.4.4
            0x00, // Dst mask 0
            0x1d, // Src mask 29
            0x10, // TCP flags
            0x01, // Direction Egress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS
    };

}
