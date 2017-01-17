package org.flowninja.common.kafka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.flowninja.common.kafka.components.KafkaComponentsConfiguration;

@Configuration
public class KafkaCommonAutoConfiguration {

    @ConditionalOnProperty(name = "collectord.kafka.enabled")
    @Import(KafkaComponentsConfiguration.class)
    @Configuration
    public static class ConditionalCommonConfiguration {

    }
}
