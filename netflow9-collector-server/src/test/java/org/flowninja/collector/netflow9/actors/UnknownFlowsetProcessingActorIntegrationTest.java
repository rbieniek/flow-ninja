package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.netflow9.actors.UnknownFlowsetProcessingActor.StoreUnknownFlowBufferRequest;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.SingleMessageTimedSinkActor;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;
import org.flowninja.common.types.Header;

import io.netty.buffer.Unpooled;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;

import akka.actor.ActorRef;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = UnknownFlowsetProcessingActorIntegrationTest.TestConfiguration.class)
public class UnknownFlowsetProcessingActorIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private SpringActorProducer springActorProducer;

    private CompletableFuture<Optional<TemplateDecoderActor.DataTemplateDecoderRequest>> dataTemplateCompletion;
    private CompletableFuture<Optional<TemplateDecoderActor.OptionsTemplateDecoderRequest>> optionsTemplateCompletion;

    private ActorRef dataUnkonwnFlowsetActor;
    private ActorRef optionsUnkonwnFlowsetActor;

    @Before
    public void createActors() {
        dataTemplateCompletion = new CompletableFuture<>();
        optionsTemplateCompletion = new CompletableFuture<>();

        final ActorRef dataTemplateDecoderActor = springActorProducer.createActor(
                SingleMessageTimedSinkActor.class,
                TemplateDecoderActor.DataTemplateDecoderRequest.class,
                dataTemplateCompletion,
                5);
        final ActorRef optionsTemplateDecoderActor = springActorProducer.createActor(
                SingleMessageTimedSinkActor.class,
                TemplateDecoderActor.OptionsTemplateDecoderRequest.class,
                optionsTemplateCompletion,
                5);

        dataUnkonwnFlowsetActor = springActorProducer
                .createActor(UnknownFlowsetProcessingActor.class, dataTemplateDecoderActor);
        optionsUnkonwnFlowsetActor = springActorProducer
                .createActor(UnknownFlowsetProcessingActor.class, optionsTemplateDecoderActor);
    }

    @Test
    public void shouldDeliverUnknownFlowsetOnLaterDataTemplateArrival() throws Exception {
        dataUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);
        dataUnkonwnFlowsetActor.tell(
                TemplateRegistryActor.DataTemplateAvailableRequest.builder()
                        .dataTemplate(DataTemplate.builder().flowsetId(256).build())
                        .build(),
                null);

        final Optional<TemplateDecoderActor.DataTemplateDecoderRequest> m = dataTemplateCompletion.get();

        assertThat(m).isPresent();
        assertThat(m.get().getDataTemplate()).isNotNull();
        assertThat(m.get().getDataTemplate().getFlowsetId()).isEqualTo(256);
        assertThat(m.get().getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(m.get().getFlowBuffer()).isNotNull();
        assertThat(m.get().getFlowBuffer().getFlowSetId()).isEqualTo(256);
        assertThat(m.get().getFlowBuffer().getHeader())
                .isEqualTo(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
    }

    @Test
    public void shouldDeliverUnknownFlowsetOnLaterOptionsTemplateArrival() throws Exception {
        optionsUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);
        optionsUnkonwnFlowsetActor.tell(
                TemplateRegistryActor.OptionsTemplateAvailableRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().flowsetId(256).build())
                        .build(),
                null);

        assertThat(optionsTemplateCompletion.get()).isPresent();

        final TemplateDecoderActor.OptionsTemplateDecoderRequest m = optionsTemplateCompletion.get().get();

        assertThat(m.getOptionsTemplate()).isNotNull();
        assertThat(m.getOptionsTemplate().getFlowsetId()).isEqualTo(256);
        assertThat(m.getPeerAddress()).isEqualTo(InetAddress.getLoopbackAddress());
        assertThat(m.getFlowBuffer()).isNotNull();
        assertThat(m.getFlowBuffer().getFlowSetId()).isEqualTo(256);
        assertThat(m.getFlowBuffer().getHeader())
                .isEqualTo(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1).sysUpTime(0).unixSeconds(0).build());
    }

    @Test
    public void shouldNotDeliverUnknownFlowsetOnLaterMismatchingDataTemplateArrival() throws Exception {
        dataUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);
        dataUnkonwnFlowsetActor.tell(
                TemplateRegistryActor.DataTemplateAvailableRequest.builder()
                        .dataTemplate(DataTemplate.builder().flowsetId(257).build())
                        .build(),
                null);

        final Optional<TemplateDecoderActor.DataTemplateDecoderRequest> m = dataTemplateCompletion.get();

        assertThat(m).isNotPresent();
    }

    @Test
    public void shouldNotDeliverUnknownFlowsetOnLaterMismatchOptionsTemplateArrival() throws Exception {
        optionsUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);
        optionsUnkonwnFlowsetActor.tell(
                TemplateRegistryActor.OptionsTemplateAvailableRequest.builder()
                        .optionsTemplate(OptionsTemplate.builder().flowsetId(257).build())
                        .build(),
                null);

        assertThat(optionsTemplateCompletion.get()).isNotPresent();
    }

    @Test
    public void shouldNotDeliverUnknownFlowsetOnDataTemplateNotArriving() throws Exception {
        dataUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);

        final Optional<TemplateDecoderActor.DataTemplateDecoderRequest> m = dataTemplateCompletion.get();

        assertThat(m).isNotPresent();
    }

    @Test
    public void shouldNotDeliverUnknownFlowsetOnOptionsTemplateNotArriving() throws Exception {
        optionsUnkonwnFlowsetActor.tell(
                StoreUnknownFlowBufferRequest.builder()
                        .peerAddress(InetAddress.getLoopbackAddress())
                        .flowBuffer(FlowBuffer.builder()
                                .buffer(Unpooled.buffer())
                                .flowSetId(256)
                                .header(
                                        Header.builder()
                                                .recordCount(1)
                                                .sequenceNumber(1)
                                                .sourceId(1)
                                                .sysUpTime(0)
                                                .unixSeconds(0)
                                                .build())
                                .build())
                        .build(),
                null);

        assertThat(optionsTemplateCompletion.get()).isNotPresent();
    }

    @TestConfig
    @Import({ AkkaConfiguration.class, ActorsTestConfiguration.class })
    @ComponentScan(basePackageClasses = TemplateDecoderActor.class)
    public static class TestConfiguration {
    }
}
