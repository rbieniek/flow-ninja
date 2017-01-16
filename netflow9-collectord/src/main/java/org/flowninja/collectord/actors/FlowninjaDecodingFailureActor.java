package org.flowninja.collectord.actors;

import org.flowninja.collector.common.netflow9.actors.TemplateDecodingFailureMessage;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;

@AkkaComponent
public class FlowninjaDecodingFailureActor extends UntypedActor {

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(TemplateDecodingFailureMessage.class, m -> {
        }).unhandled(m -> unhandled(m));
    }

}
