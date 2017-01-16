package org.flowninja.collectord.actors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.common.akka.SpringActorProducer;

import akka.actor.ActorRef;

@Configuration
@ComponentScan(basePackageClasses = FlowninjaActorsConfiguration.class)
public class FlowninjaActorsConfiguration {

    @Bean
    public SinkActorsProvider sinkActorsProvider(final SpringActorProducer springActorProducer) {
        final ActorRef targetDistributionActor = springActorProducer.createActor(FlowninjaTargetDistributionActor.class);

        return SinkActorsProvider.builder()
                .dataFlowActor(targetDistributionActor)
                .optionsFlowActor(targetDistributionActor)
                .decodingFailureActor(springActorProducer.createActor(FlowninjaDecodingFailureActor.class))
                .serverEventActor(springActorProducer.createActor(FlowninjaServerEventActor.class))
                .build();
    }
}
