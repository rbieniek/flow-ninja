package org.flowninja.common.akka;

import static org.flowninja.common.akka.AkkaSpringExtension.SPRING_EXTENSION_PROVIDER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class SpringActorProducer {
	private final ActorSystem actorSystem;

	public ActorRef createActor(final Class<? extends Actor> actorBeanClass) {
		return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass));
	}

	public ActorRef createActor(final Class<? extends Actor> actorBeanClass, final Object... args) {
		return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass, args));
	}

	public ActorRef createActor(final String actorBeanName) {
		return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanName));
	}

	public ActorRef createActor(final String actorBeanName, final Object... args) {
		return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanName, args));
	}
}
