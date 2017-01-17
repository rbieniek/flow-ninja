package org.flowninja.common.kafka;

import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "zookeeper.cluster")
@ConditionalOnProperty(prefix = "zookeeper.cluster", name = "servers")
public class ZookeeperClusterProperties {

    private List<ZookeeperHostProperties> servers = new LinkedList<>();
}
