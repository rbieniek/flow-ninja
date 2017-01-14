package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;
import java.util.Arrays;
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
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.SingleMessageSinkActor;
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
import static org.mockito.Mockito.when;

import static java.util.concurrent.TimeUnit.SECONDS;

import akka.actor.ActorRef;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = TemplateDecoderActorIntegrationTest.TestConfiguration.class)
public class TemplateDecoderActorIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

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
    public void resetCompletions() {
        completions.reset();
    }

    @Test
    public void shouldCreateOneDataFlowMessageOnOneDecodedBuffer() throws Exception {
        templateDecoderActor.tell(
                TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                        .dataTemplate(DataTemplate.builder().flowsetId(256).build())
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

        assertThat(completions.getDataFlowCompletion().get()).isNotNull();
        assertThat(completions.getOptionsFlowCompletion().isDone()).isFalse();
        assertThat(completions.getDecodingFailureCompletion().isDone()).isFalse();
    }

    @Test
    public void shouldCreateOneOptionsFlowMessageOnOneDecodedBuffer() throws Exception {
        templateDecoderActor.tell(
                TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().flowsetId(256).build())
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

        assertThat(completions.getDataFlowCompletion().isDone()).isFalse();
        assertThat(completions.getOptionsFlowCompletion().get()).isNotNull();
        assertThat(completions.getDecodingFailureCompletion().isDone()).isFalse();
    }

    @Test
    public void shouldCreateOneFailureMessageOnFailedDataTemplate() throws Exception {
        templateDecoderActor.tell(
                TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                        .dataTemplate(DataTemplate.builder().flowsetId(1).build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(1)
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

        assertThat(completions.getDataFlowCompletion().isDone()).isFalse();
        assertThat(completions.getOptionsFlowCompletion().isDone()).isFalse();

        final TemplateDecodingFailureMessage msg = completions.getDecodingFailureCompletion().get();

        assertThat(msg.getDataTemplate()).isNotNull();
        assertThat(msg.getOptionsTemplate()).isNull();
        assertThat(msg.getFlowSetId()).isEqualTo(1);
        assertThat(msg.getHeader())
                .isEqualTo(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
        assertThat(msg.getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(msg.getPayload()).isEqualTo(new byte[] { 0x00, 0x01, 0x02, 0x04 });
        assertThat(msg.getReason().getMessage()).isEqualTo("expected failure");
    }

    @Test
    public void shouldCreateOneFailureMessageOnFailedOptionsTemplate() throws Exception {
        templateDecoderActor.tell(
                TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().flowsetId(1).build())
                        .flowBuffer(
                                FlowBuffer.builder()
                                        .flowSetId(1)
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

        assertThat(completions.getDataFlowCompletion().isDone()).isFalse();
        assertThat(completions.getOptionsFlowCompletion().isDone()).isFalse();

        final TemplateDecodingFailureMessage msg = completions.getDecodingFailureCompletion().get();

        assertThat(msg.getDataTemplate()).isNull();
        assertThat(msg.getOptionsTemplate()).isNotNull();
        assertThat(msg.getFlowSetId()).isEqualTo(1);
        assertThat(msg.getHeader())
                .isEqualTo(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
        assertThat(msg.getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(msg.getPayload()).isEqualTo(new byte[] { 0x00, 0x01, 0x02, 0x04 });
        assertThat(msg.getReason().getMessage()).isEqualTo("expected failure");
    }

    @Getter
    public static class CompletionHolder implements InitializingBean {

        private CompletableFuture<DataFlowMessage> dataFlowCompletion;
        private CompletableFuture<OptionsFlowMessage> optionsFlowCompletion;
        private CompletableFuture<TemplateDecodingFailureMessage> decodingFailureCompletion;

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
    @ComponentScan(basePackageClasses = TemplateDecoderActor.class)
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
                                    SingleMessageSinkActor.class,
                                    DataFlowMessage.class,
                                    completionHolder.getDataFlowCompletion()))
                    .optionsFlowActor(
                            springActorProducer.createActor(
                                    SingleMessageSinkActor.class,
                                    OptionsFlowMessage.class,
                                    completionHolder.getOptionsFlowCompletion()))
                    .decodingFailureActor(
                            springActorProducer.createActor(
                                    SingleMessageSinkActor.class,
                                    TemplateDecodingFailureMessage.class,
                                    completionHolder.getDecodingFailureCompletion()))
                    .build();
        }

        @Bean
        public TemplateDecoder templateDecoder() {
            final TemplateDecoder templateDecoder = mock(TemplateDecoder.class);

            when(templateDecoder.decodeDataTemplate(any(InetAddress.class), any(FlowBuffer.class), any(DataTemplate.class)))
                    .then(args -> {
                        final FlowBuffer flowBuffer = args.getArgumentAt(1, FlowBuffer.class);
                        final DataTemplate dataTemplate = args.getArgumentAt(2, DataTemplate.class);

                        if (flowBuffer.getFlowSetId() == dataTemplate.getFlowsetId()) {
                            if (flowBuffer.getFlowSetId() == 256) {
                                return Arrays.asList(DataFlow.builder().build());
                            } else {
                                throw new RuntimeException("expected failure");
                            }
                        } else {
                            throw new RuntimeException(
                                    "Flowset ID mismatch, flowbuffer: " + flowBuffer.getFlowSetId() + ", template: "
                                            + dataTemplate.getFlowsetId());
                        }
                    });
            when(
                    templateDecoder
                            .decodeOptionsTemplate(any(InetAddress.class), any(FlowBuffer.class), any(OptionsTemplate.class)))
                                    .then(args -> {
                                        final FlowBuffer flowBuffer = args.getArgumentAt(1, FlowBuffer.class);
                                        final OptionsTemplate optionsTemplate = args.getArgumentAt(2, OptionsTemplate.class);

                                        if (flowBuffer.getFlowSetId() == optionsTemplate.getFlowsetId()) {
                                            if (flowBuffer.getFlowSetId() == 256) {
                                                return Arrays.asList(OptionsFlow.builder().build());
                                            } else {
                                                throw new RuntimeException("expected failure");
                                            }
                                        } else {
                                            throw new RuntimeException(
                                                    "Flowset ID mismatch, flowbuffer: " + flowBuffer.getFlowSetId()
                                                            + ", template: " + optionsTemplate.getFlowsetId());
                                        }
                                    });

            return templateDecoder;
        }

    }
}
