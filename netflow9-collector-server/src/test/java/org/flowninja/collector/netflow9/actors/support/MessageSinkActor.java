package org.flowninja.collector.netflow9.actors.support;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import lombok.RequiredArgsConstructor;

import akka.actor.UntypedActor;

@AkkaComponent
@RequiredArgsConstructor
public class MessageSinkActor<T> extends UntypedActor {

    private final Class<T> messageClass;
    private final int messageThreshold;
    private final CompletableFuture<List<T>> future;

    private List<T> collectedMessages = new LinkedList<>();

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(messageClass, m -> {
            collectedMessages.add(m);

            if (collectedMessages.size() == messageThreshold) {
                future.complete(collectedMessages);
            }
        }).unhandled(m -> unhandled(m));
    }

}
