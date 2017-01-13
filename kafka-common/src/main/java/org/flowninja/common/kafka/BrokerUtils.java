package org.flowninja.common.kafka;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

/*
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class KafkaTopicCreationInJava
{
    public static void main(String[] args) throws Exception {
        ZkClient zkClient = null;
        ZkUtils zkUtils = null;
        try {
            String zookeeperHosts = "192.168.20.1:2181"; // If multiple zookeeper then -> String zookeeperHosts = "192.168.20.1:2181,192.168.20.2:2181";
            int sessionTimeOutInMs = 15 * 1000; // 15 secs
            int connectionTimeOutInMs = 10 * 1000; // 10 secs

            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);
            zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);

            String topicName = "testTopic";
            int noOfPartitions = 2;
            int noOfReplication = 3;
            Properties topicConfiguration = new Properties();

            AdminUtils.createTopic(zkUtils, topicName, noOfPartitions, noOfReplication, topicConfiguration);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }
} */
@RequiredArgsConstructor
public class BrokerUtils implements InitializingBean {
    private final ZookeeperClusterProperties zookeeperClusterProperties;
    private final KafkaBrokerClusterProperties kafkaBrokerClusterProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public String bootstrapServers() {
        return StringUtils.join(kafkaBrokerClusterProperties.getBrokers().stream().map(bp -> "PLAINTEXT://" + bp.getBindAddr().getHostAddress() + ":" + Integer.toString(bp.getPortNumber())).collect(Collectors.toList()),",");
    }
}
