package org.flowninja.common.kafka;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

@RequiredArgsConstructor
public class EmbeddedKafkaBroker implements InitializingBean, DisposableBean {
    private final KafkaBrokerHostProperties kafkaProperties;
    private final ZookeeperHostProperties zookeeperProperties;

    private KafkaServerStartable kafkaServer;

    @Override
    public void destroy() throws Exception {
        kafkaServer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final File zookeeperTemp = Files.createTempDirectory(EmbeddedKafkaBroker.class.getSimpleName()).toFile();
        final File zookeeperLog = new File(zookeeperTemp, "log");

        zookeeperTemp.deleteOnExit();
        zookeeperLog.deleteOnExit();

        final Map<String, Object> props = new HashMap<>();


        props.put("log.dir", zookeeperLog.getAbsolutePath());
        props.put("listeners", "PLAINTEXT://" + kafkaProperties.getBindAddr().getHostAddress() + ":" + kafkaProperties.getPortNumber());
        props.put("advertised.listeners", "PLAINTEXT://" + kafkaProperties.getBindAddr().getHostAddress() + ":" + kafkaProperties.getPortNumber());
        props.put("zookeeper.connect", zookeeperProperties.getBindAddr().getHostAddress() + ":" + zookeeperProperties.getPortNumber());

        kafkaServer = new KafkaServerStartable(new KafkaConfig(props));
        kafkaServer.startup();
    };

}
