package org.flowninja.collectord.actors;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;

@AkkaComponent
public class FlowninjaTargetDistributionActor extends UntypedActor {

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(DataFlowMessage.class, m -> {
        }).onType(OptionsFlowMessage.class, m -> {
        }).unhandled(m -> unhandled(m));
    }

}
