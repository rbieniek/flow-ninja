package org.flowninja.common.akka;

import org.springframework.context.ApplicationContext;

import akka.actor.AbstractExtensionId;
import akka.actor.Actor;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.Props;

public class AkkaSpringExtension extends AbstractExtensionId<AkkaSpringExtension.SpringExt> {
	public static final AkkaSpringExtension SPRING_EXTENSION_PROVIDER = new AkkaSpringExtension();

	@Override
	public SpringExt createExtension(final ExtendedActorSystem system) {
		return new SpringExt();
	}

	public static class SpringExt implements Extension {
		private volatile ApplicationContext applicationContext;

		public void initialize(final ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		public Props props(final String actorBeanName) {
			return Props.create(SpringActorByNameProducer.class, applicationContext, actorBeanName);
		}

		public Props props(final String actorBeanName, final Object... args) {
			return Props.create(SpringActorByNameProducer.class, applicationContext, actorBeanName, args);
		}

		public Props props(final Class<? extends Actor> actorBeanClass) {
			return Props.create(SpringActorByClassProducer.class, applicationContext, actorBeanClass);
		}

		public Props props(final Class<? extends Actor> actorBeanClass, final Object... args) {
			return Props.create(SpringActorByClassProducer.class, applicationContext, actorBeanClass, args);
		}
	}
}
