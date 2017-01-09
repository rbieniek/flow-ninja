package org.flowninja.collector.netflow9.actors.support;

import java.util.concurrent.CompletableFuture;

import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;
import lombok.RequiredArgsConstructor;

@AkkaComponent
@RequiredArgsConstructor
public class SingleMessageSinkActor<T> extends UntypedActor {

	private final Class<T> messageClass;
	private final CompletableFuture<T> future;

	@Override
	public void onReceive(final Object message) throws Throwable {
		ActorUtils.withMessage(message).onType(messageClass, m -> {
			future.complete(m);
		}).unhandled(m -> unhandled(m));
	}

}
