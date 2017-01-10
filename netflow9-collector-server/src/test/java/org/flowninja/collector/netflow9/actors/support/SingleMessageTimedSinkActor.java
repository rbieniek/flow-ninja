package org.flowninja.collector.netflow9.actors.support;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.UntypedActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AkkaComponent
@RequiredArgsConstructor
@Slf4j
public class SingleMessageTimedSinkActor<T> extends UntypedActor {

	private final Class<T> messageClass;
	private final CompletableFuture<Optional<T>> future;
	private final int timeout;

	@Override
	public void onReceive(final Object message) throws Throwable {
		ActorUtils.withMessage(message).onType(messageClass, m -> {
			future.complete(Optional.of(m));
		}).unhandled(m -> unhandled(m));
	}

	@Override
	public void preStart() throws Exception {
		new Thread(() -> {
			try {
				Thread.sleep(timeout * 1000L);
			} catch (InterruptedException e) {
				log.info("Wait thread interrupted", e);
			}

			future.complete(Optional.empty());
		}).start();
	}

}
