package org.flowninja.collector.netflow9.actors;

import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;
import org.flowninja.common.akka.SpringActorProducer;
import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AkkaComponent
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class Netflow9DatagramActor extends UntypedActor {

	private final SpringActorProducer springActorProducer;

	private ActorRef templateDecoderActor;
	private ActorRef templateRegistryActor;
	private ActorRef unknownFlowsetProcessingActor;

	@Override
	public void preStart() throws Exception {
		templateDecoderActor = springActorProducer.createActor(TemplateDecoderActor.class);
		unknownFlowsetProcessingActor = springActorProducer.createActor(UnknownFlowsetProcessingActor.class,
				templateDecoderActor);
		templateRegistryActor = springActorProducer.createActor(TemplateRegistryActor.class);

		templateRegistryActor.tell(
				TemplateRegistryActor.AddNotifiedRequest.builder().notified(unknownFlowsetProcessingActor).build(),
				getSelf());
	}

	@Override
	public void onReceive(final Object message) throws Throwable {
		ActorUtils.withMessage(message).onType(ProcessNetflowDatagramRequest.class, m -> {
			// intentionally left blank
		}).onType(NetworkServerShutdownMessage.class, m -> {
			// intentionally left blank
		}).unhandled(m -> unhandled(m));
	}

	@Builder
	@Getter
	public static class ProcessNetflowDatagramRequest {

		private Netflow9DecodedDatagram datagram;
	}
}
