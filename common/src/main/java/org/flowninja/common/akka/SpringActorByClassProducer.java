package org.flowninja.common.akka;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

public class SpringActorByClassProducer implements IndirectActorProducer {
	private ApplicationContext applicationContext;

	private Class<? extends Actor> beanActorClass;
	private Object[] args;

	public SpringActorByClassProducer(final ApplicationContext applicationContext,
			final Class<? extends Actor> beanActorClass) {
		this.applicationContext = applicationContext;
		this.beanActorClass = beanActorClass;
	}

	public SpringActorByClassProducer(final ApplicationContext applicationContext,
			final Class<? extends Actor> beanActorClass, final Object... args) {
		this.applicationContext = applicationContext;
		this.beanActorClass = beanActorClass;
		this.args = args;
	}

	@Override
	public Actor produce() {
		if (args != null) {
			return applicationContext.getBean(beanActorClass, args);
		} else {
			return applicationContext.getBean(beanActorClass);
		}
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return beanActorClass;
	}
}
