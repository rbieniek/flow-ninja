package org.flowninja.common.kafka.components;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.InitializingBean;

import org.flowninja.common.kafka.config.KafkaBrokerClusterProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProducerConfig implements InitializingBean {

    private final KafkaBrokerClusterProperties kafkaBrokerClusterProperties;

    private Map<String, Object> producerConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        producerConfig.put("bootstrap.servers", StringUtils.join(kafkaBrokerClusterProperties.getBrokers()
                .stream()
                .map(
                        bp -> "PLAINTEXT://" + bp.getHost().getHostAddress() + ":" + Integer.toString(bp.getPortNumber()))
                .collect(Collectors.toList()), ","));
        producerConfig.put("acks", "all");
        producerConfig.put("retries", 0);
        producerConfig.put("batch.size", 16384);
        producerConfig.put("linger.ms", 1);
        producerConfig.put("buffer.memory", 33554432);
    }

    public Map<String, Object> producerConfig() {
        return new HashMap<>(producerConfig);
    }
}
