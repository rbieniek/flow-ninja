package org.flowninja.common.kafka.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.flowninja.common.kafka.config.KafkaBrokerClusterProperties;
import org.flowninja.common.kafka.config.ZookeeperClusterProperties;

@Configuration
@ComponentScan(basePackageClasses = KafkaComponentsConfiguration.class)
public class KafkaComponentsConfiguration {

    @Bean
    @Autowired
    public BrokerUtils brokerUtils(
            final ZookeeperClusterProperties zookeeperClusterProperties,
            final KafkaBrokerClusterProperties kafkaBrokerClusterProperties) {
        return new BrokerUtils(zookeeperClusterProperties, kafkaBrokerClusterProperties);
    }

    @Bean
    @Autowired
    public ProducerConfigFactoryBean producerConfigFactoryBean(
            final KafkaBrokerClusterProperties kafkaBrokerClusterProperties) {
        return new ProducerConfigFactoryBean(kafkaBrokerClusterProperties);
    }

}
