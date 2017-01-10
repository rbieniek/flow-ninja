package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collector.common.netflow9.actors.TemplateDecodingFailureMessage;
import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.DataTemplateField;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.SingleMessageTimedSinkActor;
import org.flowninja.collector.netflow9.components.InetSocketAddressPeerAddressMapper;
import org.flowninja.collector.netflow9.components.PeerAddressMapper;
import org.flowninja.collector.netflow9.components.TemplateDecoder;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;

import io.netty.buffer.Unpooled;

import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;

import akka.actor.ActorRef;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = Netflow9DatagramActorIntegrationTest.TestConfiguration.class)
public class Netflow9DatagramActorIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private SpringActorProducer springActorProducer;

    @Autowired
    private CompletionHolder completions;

    private ActorRef netflowDatagramActor;

    @Before
    public void createTemplateDecoderActor() {
        netflowDatagramActor = springActorProducer.createActor(Netflow9DatagramActor.class);
    }

    @After
    public void resetCompletions() {
        completions.reset();
    }

    @Test
    public void shouldDecodeDataFlowWhenTemplateAndFlowInOneDatagram() throws Exception {
        netflowDatagramActor.tell(
                Netflow9DatagramActor.ProcessNetflowDatagramRequest.builder()
                        .datagram(Netflow9DecodedDatagram.builder()
                                .peerAddress(InetAddress.getLoopbackAddress())
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .templates(Arrays.asList(DataTemplate.builder()
                                        .flowsetId(256)
                                        .fields(
                                                Arrays.asList(
                                                        DataTemplateField.builder()
                                                                .length(4)
                                                                .type(FieldType.LAST_SWITCHED)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(4)
                                                                .type(FieldType.FIRST_SWITCHED)
                                                                .build(),
                                                        DataTemplateField.builder().length(4).type(FieldType.IN_BYTES).build(),
                                                        DataTemplateField.builder().length(4).type(FieldType.IN_PKTS).build(),
                                                        DataTemplateField.builder()
                                                                .length(2)
                                                                .type(FieldType.INPUT_SNMP)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(2)
                                                                .type(FieldType.OUTPUT_SNMP)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(4)
                                                                .type(FieldType.IPV4_SRC_ADDR)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(4)
                                                                .type(FieldType.IPV4_DST_ADDR)
                                                                .build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.PROTOCOL).build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.SRC_TOS).build(),
                                                        DataTemplateField.builder()
                                                                .length(2)
                                                                .type(FieldType.L4_SRC_PORT)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(2)
                                                                .type(FieldType.L4_DST_PORT)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(1)
                                                                .type(FieldType.FLOW_SAMPLER_ID)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(1)
                                                                .type(FieldType.FLOW_CLASS)
                                                                .build(),
                                                        DataTemplateField.builder()
                                                                .length(4)
                                                                .type(FieldType.IPV4_NEXT_HOP)
                                                                .build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.DST_MASK).build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.SRC_MASK).build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.TCP_FLAGS).build(),
                                                        DataTemplateField.builder().length(1).type(FieldType.DIRECTION).build(),
                                                        DataTemplateField.builder().length(2).type(FieldType.DST_AS).build(),
                                                        DataTemplateField.builder().length(2).type(FieldType.SRC_AS).build()))
                                        .build()))
                                .flows(
                                        Arrays.asList(FlowBuffer.builder()
                                                .flowSetId(256)
                                                .header(
                                                        Header.builder()
                                                                .recordCount(1)
                                                                .sequenceNumber(1)
                                                                .sourceId(1)
                                                                .sysUpTime(0)
                                                                .unixSeconds(0)
                                                                .build())
                                                .buffer(Unpooled.wrappedBuffer(new byte[] {
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
                                                })).build())).build()).build(), null);

        Optional<DataFlowMessage> m = completions.getDataFlowCompletion().get();

        assertThat(m).isPresent();

        assertThat(m.get().getDataFlow()).isNotNull();
        assertThat(m.get().getDataFlow().getHeader())
                .isEqualTo(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());

        assertThat(m.get().getDataFlow().getUuid()).isNotNull();
        assertThat(m.get().getDataFlow().getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(m.get().getDataFlow().getRecords()).hasSize(21);
    }

    @Getter
    public static class CompletionHolder implements InitializingBean {

        private CompletableFuture<Optional<DataFlowMessage>> dataFlowCompletion;
        private CompletableFuture<Optional<OptionsFlowMessage>> optionsFlowCompletion;
        private CompletableFuture<Optional<TemplateDecodingFailureMessage>> decodingFailureCompletion;

        public void reset() {
            dataFlowCompletion = new CompletableFuture<>();
            optionsFlowCompletion = new CompletableFuture<>();
            decodingFailureCompletion = new CompletableFuture<>();

        }

        @Override
        public void afterPropertiesSet() throws Exception {
            reset();
        }
    }

    @TestConfig
    @Import({ AkkaConfiguration.class, ActorsTestConfiguration.class })
    @ComponentScan(basePackageClasses = { TemplateDecoderActor.class, TemplateDecoder.class })
    public static class TestConfiguration {

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
                    .build();
        }

        @Bean
        public PeerAddressMapper peerAddressMapper() {
            return new InetSocketAddressPeerAddressMapper();
        }
    }

}
