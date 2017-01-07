package org.flowninja.collector.netflow9.actors;

import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;
import lombok.Builder;
import lombok.Getter;

@AkkaComponent
public class Netflow9DatagramActor extends UntypedActor {

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
