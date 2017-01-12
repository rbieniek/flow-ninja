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
    public EmbeddedZookeeperProperties embeddedZookeeperProperties() {
        final EmbeddedZookeeperProperties properties = new EmbeddedZookeeperProperties();

        properties.setBindAddr(InetAddress.getLoopbackAddress());
        properties.setPortNumber(SocketUtils.findAvailableTcpPort(32768));

        return properties;
    }

    @Bean
    public EmbeddedKafkaBrokerProperties embeddedKafkaBrokerProperties() {
        final EmbeddedKafkaBrokerProperties properties = new EmbeddedKafkaBrokerProperties();

        properties.setBindAddr(InetAddress.getLoopbackAddress());
        properties.setPortNumber(SocketUtils.findAvailableTcpPort(32768));

        return properties;
    }

    @Bean
    @Autowired
    @DependsOn("embeddedZookeeper")
    public EmbeddedKafkaBroker embeddedKafkaBroker(final EmbeddedKafkaBrokerProperties kafkaProperties,final EmbeddedZookeeperProperties zookeeperProperties) {
        return new EmbeddedKafkaBroker(kafkaProperties, zookeeperProperties);
    }

    @Bean
    @Autowired
    public EmbeddedZookeeper embeddedZookeeper(final EmbeddedZookeeperProperties zookeeperProperties) {
        return new EmbeddedZookeeper(zookeeperProperties);
    }
}
