package org.flowninja.collectord.components;

import akka.actor.ActorRef;

public interface TargetDistributionActorProvider {

    ActorRef providerActor();
}
