package org.flowninja.collectord.actors;

import org.flowninja.collector.common.netflow9.actors.NetworkServerShutdownMessage;
import org.flowninja.collector.common.netflow9.actors.NetworkServerStartupMessage;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;

@AkkaComponent
public class FlowninjaServerEventActor extends UntypedActor {

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(NetworkServerStartupMessage.class, m -> {
        }).onType(NetworkServerShutdownMessage.class, m -> {
        }).unhandled(m -> unhandled(m));
    }

}
