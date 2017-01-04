package org.flowninja.common.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;

@Configuration
public class AkkaConfiguration {
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ActorSystem actorSystem() {
		ActorSystem system = ActorSystem.create("md-openapi-generator");

		AkkaSpringExtension.SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);

		return system;
	}
}
