package org.flowninja.common.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=KafkaCommonConfiguration.class)
public class KafkaCommonConfiguration {

    @Bean
    @ConditionalOnBean(value={ ZookeeperClusterProperties.class, KafkaBrokerClusterProperties.class })
    @Autowired
    public BrokerUtils brokerUtils(final ZookeeperClusterProperties zookeeperClusterProperties, final KafkaBrokerClusterProperties kafkaBrokerClusterProperties) {
        return new BrokerUtils(zookeeperClusterProperties, kafkaBrokerClusterProperties);
    }
}
