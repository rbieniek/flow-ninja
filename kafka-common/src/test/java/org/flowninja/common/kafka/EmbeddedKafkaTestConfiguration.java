package org.flowninja.common.kafka;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.SocketUtils;

import org.flowninja.common.TestConfig;

@TestConfig
public class EmbeddedKafkaTestConfiguration {

    @Bean
    public ZookeeperHostProperties zookeeperHostProperties() {
        final ZookeeperHostProperties properties = new ZookeeperHostProperties();

        properties.setBindAddr(InetAddress.getLoopbackAddress());
        properties.setPortNumber(SocketUtils.findAvailableTcpPort(32768));

        return properties;
    }

    @Bean
    public KafkaBrokerHostProperties kafkaBrokerHostProperties() {
        final KafkaBrokerHostProperties properties = new KafkaBrokerHostProperties();

        properties.setBindAddr(InetAddress.getLoopbackAddress());
        properties.setPortNumber(SocketUtils.findAvailableTcpPort(32768));

        return properties;
    }

    @Bean
    @Autowired
    @DependsOn("embeddedZookeeper")
    public EmbeddedKafkaBroker embeddedKafkaBroker(final KafkaBrokerHostProperties kafkaProperties,final ZookeeperHostProperties zookeeperProperties) {
        return new EmbeddedKafkaBroker(kafkaProperties, zookeeperProperties);
    }

    @Bean
    @Autowired
    public EmbeddedZookeeper embeddedZookeeper(final ZookeeperHostProperties zookeeperProperties) {
        return new EmbeddedZookeeper(zookeeperProperties);
    }
}
