package org.flowninja.common.kafka.config;

import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix="kafka.cluster")
public class KafkaBrokerClusterProperties {
    private List<KafkaBrokerHostProperties> brokers = new LinkedList<>();
}
