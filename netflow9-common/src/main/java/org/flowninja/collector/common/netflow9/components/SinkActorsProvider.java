package org.flowninja.collector.common.netflow9.components;

import akka.actor.ActorRef;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SinkActorsProvider {
	private ActorRef optionsFlowActor;
	private ActorRef dataFlowActor;
	private ActorRef decodingFailureActor;
}
