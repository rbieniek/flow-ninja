package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collector.common.netflow9.actors.TemplateDecodingFailureMessage;
import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.MessageSinkActor;
import org.flowninja.collector.netflow9.components.TemplateDecoder;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;

import io.netty.buffer.Unpooled;

import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import static java.util.concurrent.TimeUnit.SECONDS;

import akka.actor.ActorRef;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = TemplateDecoderActorIntegrationTest.TestConfiguration.class)
public class TemplateDecoderActorIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private TemplateDecoder templateDecoder;

    @Autowired
    private SpringActorProducer springActorProducer;

    @Autowired
    private CompletionHolder completions;

    private ActorRef templateDecoderActor;

    @Before
    public void createTemplateDecoderActor() {
        templateDecoderActor = springActorProducer.createActor(TemplateDecoderActor.class);
    }

    @After
    public void clearMocks() {
        reset(templateDecoder);
    }

    @Test
    public void shouldCreateOneDataFlowMessageOnOneDecodedBuffer() {
        when(templateDecoder.decodeDataTemplate(any(InetAddress.class), any(FlowBuffer.class), any(DataTemplate.class)))
                .thenReturn(Arrays.asList(DataFlow.builder().build()));
        when(templateDecoder.decodeOptionsTemplate(any(InetAddress.class), any(FlowBuffer.class), any(OptionsTemplate.class)))
                .thenThrow(new RuntimeException("should not reach this"));

        templateDecoderActor.tell(
                TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                        .dataTemplate(DataTemplate.builder().build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(256)
                                        .buffer(Unpooled.wrappedBuffer(new byte[] { 0x00, 0x01, 0x02, 0x04 }))
                                        .header(
                                                Header.builder()
                                                        .recordCount(1)
                                                        .sequenceNumber(1)
                                                        .sourceId(1)
                                                        .sysUpTime(0)
                                                        .unixSeconds(0)
                                                        .build())
                                        .build())
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .build(),
                null);

        completions.getDataFlowCompletion().whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(t).isNull();
        });
        completions.getOptionsFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getDecodingFailureCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
    }

    @Test
    public void shouldCreateOneOptionsFlowMessageOnOneDecodedBuffer() {
        when(templateDecoder.decodeDataTemplate(any(InetAddress.class), any(FlowBuffer.class), any(DataTemplate.class)))
                .thenThrow(new RuntimeException("should not reach this"));
        when(templateDecoder.decodeOptionsTemplate(any(InetAddress.class), any(FlowBuffer.class), any(OptionsTemplate.class)))
                .thenReturn(Arrays.asList(OptionsFlow.builder().build()));

        templateDecoderActor.tell(
                TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(256)
                                        .buffer(Unpooled.wrappedBuffer(new byte[] { 0x00, 0x01, 0x02, 0x04 }))
                                        .header(
                                                Header.builder()
                                                        .recordCount(1)
                                                        .sequenceNumber(1)
                                                        .sourceId(1)
                                                        .sysUpTime(0)
                                                        .unixSeconds(0)
                                                        .build())
                                        .build())
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .build(),
                null);

        completions.getDataFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getOptionsFlowCompletion().whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(t).isNull();
        });
        completions.getDecodingFailureCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
    }

    @Test
    public void shouldCreateOneFailureMessageOnFailedDataTemplate() {
        when(templateDecoder.decodeDataTemplate(any(InetAddress.class), any(FlowBuffer.class), any(DataTemplate.class)))
                .thenThrow(new RuntimeException("expected failure"));
        when(templateDecoder.decodeOptionsTemplate(any(InetAddress.class), any(FlowBuffer.class), any(OptionsTemplate.class)))
                .thenThrow(new RuntimeException("should not reach this"));

        templateDecoderActor.tell(
                TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                        .dataTemplate(DataTemplate.builder().build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(256)
                                        .buffer(Unpooled.wrappedBuffer(new byte[] { 0x00, 0x01, 0x02, 0x04 }))
                                        .header(
                                                Header.builder()
                                                        .recordCount(1)
                                                        .sequenceNumber(1)
                                                        .sourceId(1)
                                                        .sysUpTime(0)
                                                        .unixSeconds(0)
                                                        .build())
                                        .build())
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .build(),
                null);

        completions.getDataFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getOptionsFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getDecodingFailureCompletion().whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(t).isNull();

            final TemplateDecodingFailureMessage msg = l.get(0);

            assertThat(msg.getDataTemplate()).isNotNull();
            assertThat(msg.getOptionsTemplate()).isNull();
            assertThat(msg.getFlowSetId()).isEqualTo(256);
            assertThat(msg.getHeader()).isEqualTo(
                    Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
            try {
                assertThat(msg.getPeerAddress()).isEqualTo(InetAddress.getLocalHost());
            } catch (UnknownHostException e) {
                Assert.fail();
            }
            assertThat(msg.getPayload()).isEqualTo(new byte[] { 0x00, 0x01, 0x02, 0x04 });
            assertThat(msg.getReason().getMessage()).isEqualTo("expected failure");
        });
    }

    @Test
    public void shouldCreateOneFailureMessageOnFailedOptionsTemplate() {
        when(templateDecoder.decodeDataTemplate(any(InetAddress.class), any(FlowBuffer.class), any(DataTemplate.class)))
                .thenThrow(new RuntimeException("should not reach this"));
        when(templateDecoder.decodeOptionsTemplate(any(InetAddress.class), any(FlowBuffer.class), any(OptionsTemplate.class)))
                .thenThrow(new RuntimeException("expected failure"));

        templateDecoderActor.tell(
                TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(256)
                                        .buffer(Unpooled.wrappedBuffer(new byte[] { 0x00, 0x01, 0x02, 0x04 }))
                                        .header(
                                                Header.builder()
                                                        .recordCount(1)
                                                        .sequenceNumber(1)
                                                        .sourceId(1)
                                                        .sysUpTime(0)
                                                        .unixSeconds(0)
                                                        .build())
                                        .build())
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .build(),
                null);

        completions.getDataFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getOptionsFlowCompletion().whenComplete((l, t) -> {
            Assert.fail();
        });
        completions.getDecodingFailureCompletion().whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(t).isNull();

            final TemplateDecodingFailureMessage msg = l.get(0);

            assertThat(msg.getDataTemplate()).isNull();
            assertThat(msg.getOptionsTemplate()).isNotNull();
            assertThat(msg.getFlowSetId()).isEqualTo(256);
            assertThat(msg.getHeader()).isEqualTo(
                    Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
            try {
                assertThat(msg.getPeerAddress()).isEqualTo(InetAddress.getLocalHost());
            } catch (UnknownHostException e) {
                Assert.fail();
            }
            assertThat(msg.getPayload()).isEqualTo(new byte[] { 0x00, 0x01, 0x02, 0x04 });
            assertThat(msg.getReason().getMessage()).isEqualTo("expected failure");
        });
    }

    @Getter
    public static class CompletionHolder {

        private CompletableFuture<List<DataFlowMessage>> dataFlowCompletion = new CompletableFuture<>();
        private CompletableFuture<List<OptionsFlowMessage>> optionsFlowCompletion = new CompletableFuture<>();
        private CompletableFuture<List<TemplateDecodingFailureMessage>> decodingFailureCompletion = new CompletableFuture<>();
    }

    @TestConfig
    @Import({ AkkaConfiguration.class, ActorsTestConfiguration.class })
    @ComponentScan(basePackageClasses = TemplateDecoderActor.class)
    public static class TestConfiguration {

        @Bean
        public CompletionHolder completionHolder() {
            return new CompletionHolder();
        }

        @Bean
        @Autowired
        public SinkActorsProvider sinkActorsProvider(
                final SpringActorProducer springActorProducer,
                final CompletionHolder completionHolder) {
            return SinkActorsProvider.builder()
                    .dataFlowActor(
                            springActorProducer.createActor(
                                    MessageSinkActor.class,
                                    DataFlowMessage.class,
                                    1,
                                    completionHolder.getDataFlowCompletion()))
                    .optionsFlowActor(
                            springActorProducer.createActor(
                                    MessageSinkActor.class,
                                    OptionsFlowMessage.class,
                                    1,
                                    completionHolder.getOptionsFlowCompletion()))
                    .decodingFailureActor(
                            springActorProducer.createActor(
                                    MessageSinkActor.class,
                                    TemplateDecodingFailureMessage.class,
                                    1,
                                    completionHolder.getDecodingFailureCompletion()))
                    .build();
        }

        @Bean
        public TemplateDecoder templateDecoder() {
            return mock(TemplateDecoder.class);
        }

    }
}
