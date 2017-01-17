package org.flowninja.common.kafka.components;

import org.springframework.beans.factory.FactoryBean;

import org.flowninja.common.kafka.config.KafkaBrokerClusterProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProducerConfigFactoryBean implements FactoryBean<ProducerConfig> {

	private final KafkaBrokerClusterProperties kafkaBrokerClusterProperties;

	@Override
	public ProducerConfig getObject() throws Exception {
		return new ProducerConfig(kafkaBrokerClusterProperties);
	}

	@Override
	public Class<?> getObjectType() {
		return ProducerConfig.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
