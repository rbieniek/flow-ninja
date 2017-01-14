package org.flowninja.common.kafka;

import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProducerConfig implements InitializingBean {
	private final KafkaBrokerClusterProperties kafkaBrokerClusterProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

}
