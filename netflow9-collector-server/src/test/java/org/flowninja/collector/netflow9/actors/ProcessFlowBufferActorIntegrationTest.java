package org.flowninja.collector.netflow9.actors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.netflow9.actors.ProcessFlowBufferActorIntegrationTest.TestConfiguration.MockTemplateRegistryActor;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.SingleMessageSinkActor;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;
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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import io.netty.buffer.Unpooled;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ProcessFlowBufferActorIntegrationTest.TestConfiguration.class)
public class ProcessFlowBufferActorIntegrationTest {
	@Rule
	public Timeout globalTimeout = new Timeout(10, SECONDS);

	@Autowired
	private SpringActorProducer springActorProducer;

	private ActorSystem actorSystem;

	private ActorRef processFlowBufferDataActor;
	private ActorRef processFlowBufferOptionsActor;
	private ActorRef templateRegistryActor;
	private ActorRef unknownFlowsetProcessorActor;
	private ActorRef templateDecoderDataActor;
	private ActorRef templateDecoderOptionsActor;

	private CompletableFuture<TemplateDecoderActor.DataTemplateDecoderRequest> dataTemplateCompletion;
	private CompletableFuture<TemplateDecoderActor.OptionsTemplateDecoderRequest> optionsTemplateCompletion;
	private CompletableFuture<UnknownFlowsetProcessingActor.StoreUnknownFlowBufferRequest> unknownFlowsetCompletion;

	@Before
	public void createActors() {
		dataTemplateCompletion = new CompletableFuture<>();
		optionsTemplateCompletion = new CompletableFuture<>();
		unknownFlowsetCompletion = new CompletableFuture<>();

		templateRegistryActor = springActorProducer.createActor(MockTemplateRegistryActor.class);
		unknownFlowsetProcessorActor = springActorProducer.createActor(SingleMessageSinkActor.class,
				UnknownFlowsetProcessingActor.StoreUnknownFlowBufferRequest.class, unknownFlowsetCompletion);
		templateDecoderDataActor = springActorProducer.createActor(SingleMessageSinkActor.class,
				TemplateDecoderActor.DataTemplateDecoderRequest.class, dataTemplateCompletion);
		templateDecoderOptionsActor = springActorProducer.createActor(SingleMessageSinkActor.class,
				TemplateDecoderActor.OptionsTemplateDecoderRequest.class, optionsTemplateCompletion);
		processFlowBufferDataActor = springActorProducer.createActor(ProcessFlowBufferActor.class,
				templateDecoderDataActor, templateRegistryActor, unknownFlowsetProcessorActor);
		processFlowBufferOptionsActor = springActorProducer.createActor(ProcessFlowBufferActor.class,
				templateDecoderOptionsActor, templateRegistryActor, unknownFlowsetProcessorActor);
	}

	@Test
	public void shouldSendToUnknownFLowsetActorForUnknownFlowset() throws Exception {
		processFlowBufferDataActor
				.tell(ProcessFlowBufferActor.ProcessFlowBufferRequest.builder().peerAddress(InetAddress.getLocalHost())
						.flowBuffer(FlowBuffer.builder()
								.flowSetId(1).header(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1)
										.sysUpTime(1).unixSeconds(0).build())
								.buffer(Unpooled.buffer()).build())
						.build(), null);

		unknownFlowsetCompletion.whenComplete((m, t) -> {
			assertThat(t).isNull();

			assertThat(m.getPeerAddress()).isNotNull();
			assertThat(m.getFlowBuffer()).isNotNull();
			assertThat(m.getFlowBuffer().getFlowSetId()).isEqualTo(1);
		});
		assertThat(dataTemplateCompletion.isDone()).isFalse();
		assertThat(optionsTemplateCompletion.isDone()).isFalse();
	}

	@Test
	public void shouldSendTemplateDecoderDataForKnownFlowset() throws Exception {
		processFlowBufferDataActor
				.tell(ProcessFlowBufferActor.ProcessFlowBufferRequest.builder().peerAddress(InetAddress.getLocalHost())
						.flowBuffer(FlowBuffer.builder()
								.flowSetId(256).header(Header.builder().recordCount(1).sequenceNumber(1).sourceId(1)
										.sysUpTime(1).unixSeconds(0).build())
								.buffer(Unpooled.buffer()).build())
						.build(), null);

		dataTemplateCompletion.whenCompleteAsync((m, t) -> {
			assertThat(t).isNull();

			assertThat(m.getPeerAddress()).isNotNull();
			assertThat(m.getDataTemplate()).isNotNull();
			assertThat(m.getDataTemplate().getFlowsetId()).isEqualTo(256);
			assertThat(m.getFlowBuffer()).isNotNull();
			assertThat(m.getFlowBuffer().getFlowSetId()).isEqualTo(1);
		});
		assertThat(unknownFlowsetCompletion.isDone()).isFalse();
		assertThat(optionsTemplateCompletion.isDone()).isFalse();
	}

	@TestConfig
	@Import({ AkkaConfiguration.class, ActorsTestConfiguration.class })
	@ComponentScan(basePackageClasses = TemplateDecoderActor.class)
	public static class TestConfiguration {

		@AkkaComponent
		public static class MockTemplateRegistryActor extends UntypedActor {

			@Override
			public void onReceive(final Object message) throws Throwable {
				ActorUtils.withMessage(message).onType(TemplateRegistryActor.LookupTemplateRequest.class, m -> {
					switch (m.getFlowsetId()) {
					case 256:
						getSender().tell(TemplateRegistryActor.LookupDataTemplateResponse.builder()
								.dataTemplate(DataTemplate.builder().flowsetId(m.getFlowsetId()).build()), getSelf());
						break;
					case 257:
						getSender().tell(TemplateRegistryActor.LookupOptionsTemplateResponse.builder().optionsTemplate(
								OptionsTemplate.builder().flowsetId(m.getFlowsetId()).build()), getSelf());
						break;
					default:
						getSender().tell(TemplateRegistryActor.UnknownFlowsetResponse.class, getSelf());
						break;
					}
				}).unhandled(m -> unhandled(m));
			}

		}
	}
}
