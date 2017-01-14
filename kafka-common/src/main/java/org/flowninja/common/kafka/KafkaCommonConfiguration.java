package org.flowninja.common.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = KafkaCommonConfiguration.class)
@EnableConfigurationProperties
public class KafkaCommonConfiguration {

	@Configuration
	@ConditionalOnBean(value = { ZookeeperClusterProperties.class, KafkaBrokerClusterProperties.class })
	public static class BrokerUtilsConfiguration {
		@Bean
		@Autowired
		public BrokerUtils brokerUtils(final ZookeeperClusterProperties zookeeperClusterProperties,
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

}
