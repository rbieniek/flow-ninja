package org.flowninja.collectord.actors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collectord.components.TargetDistributionActorProvider;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import lombok.RequiredArgsConstructor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

@AkkaComponent
@RequiredArgsConstructor
public class FlowninjaTargetDistributionActor extends UntypedActor {

    private final ApplicationContext applicationContext;

    private Map<String, ActorRef> targetActors = new HashMap<>();

    @Override
    public void preStart() throws Exception {
        applicationContext.getBeansOfType(TargetDistributionActorProvider.class).forEach((k, v) -> {
            targetActors.put(k, v.providerActor());
        });
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(DataFlowMessage.class, m -> {
            targetActors.forEach((k, v) -> v.tell(m, getSelf()));
        }).onType(OptionsFlowMessage.class, m -> {
            targetActors.forEach((k, v) -> v.tell(m, getSelf()));
        }).unhandled(m -> unhandled(m));
    }

}
