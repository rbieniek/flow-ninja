package org.flowninja.common.akka;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

public class SpringActorByNameProducer implements IndirectActorProducer {
	private ApplicationContext applicationContext;

	private String beanActorName;
	private Object[] args;

	public SpringActorByNameProducer(final ApplicationContext applicationContext, final String beanActorName) {
		this.applicationContext = applicationContext;
		this.beanActorName = beanActorName;
	}

	public SpringActorByNameProducer(final ApplicationContext applicationContext, final String beanActorName,
			final Object... args) {
		this.applicationContext = applicationContext;
		this.beanActorName = beanActorName;
		this.args = args;
	}

	@Override
	public Actor produce() {
		if (args != null) {
			return (Actor) applicationContext.getBean(beanActorName, args);
		} else {
			return (Actor) applicationContext.getBean(beanActorName);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext.getType(beanActorName);
	}
}
