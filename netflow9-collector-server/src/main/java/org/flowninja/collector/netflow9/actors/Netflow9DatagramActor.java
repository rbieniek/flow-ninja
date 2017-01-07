package org.flowninja.collector.netflow9.actors;

import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;
import lombok.Builder;
import lombok.Getter;

@AkkaComponent
public class Netflow9DatagramActor extends UntypedActor {

	@Override
	public void onReceive(final Object message) throws Throwable {
		// Intentionally left blank
	}

	@Builder
	@Getter
	public static class ProcessNetflowDatagramRequest {
		private Netflow9DecodedDatagram datagram;
	}
}
