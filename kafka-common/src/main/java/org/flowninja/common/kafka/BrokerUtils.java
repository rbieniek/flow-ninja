package org.flowninja.common.kafka;

import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode.Safe$;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BrokerUtils implements InitializingBean, DisposableBean {
	private final ZookeeperClusterProperties zookeeperClusterProperties;
	private final KafkaBrokerClusterProperties kafkaBrokerClusterProperties;

	private ZkClient zkClient = null;
	private ZkUtils zkUtils = null;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			int sessionTimeOutInMs = 15 * 1000; // 15 secs
			int connectionTimeOutInMs = 10 * 1000; // 10 secs

			zkClient = new ZkClient(zookeeperServers(), sessionTimeOutInMs, connectionTimeOutInMs,
					ZKStringSerializer$.MODULE$);
			zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperServers()), false);

		} catch (Exception ex) {
			log.info("Failed to create zookeeper client", ex);

			throw ex;
		}
	}

	@Override
	public void destroy() throws Exception {
		zkClient.close();
	}

	public void createTopic(final String topicName, final Optional<Integer> numberOfPartitions,
			final Optional<Integer> numberOfReplicatiopn, final Optional<Properties> topicConfiguration) {

		AdminUtils.createTopic(zkUtils, topicName, numberOfPartitions.orElse(1), numberOfReplicatiopn.orElse(1),
				topicConfiguration.orElse(new Properties()), Safe$.MODULE$);

	}

	public String bootstrapServers() {
		return StringUtils.join(kafkaBrokerClusterProperties.getBrokers().stream().map(
				bp -> "PLAINTEXT://" + bp.getBindAddr().getHostAddress() + ":" + Integer.toString(bp.getPortNumber()))
				.collect(Collectors.toList()), ",");
	}

	public String zookeeperServers() {
		return StringUtils.join(zookeeperClusterProperties.getServers().stream()
				.map(bp -> bp.getBindAddr().getHostAddress() + ":" + Integer.toString(bp.getPortNumber()))
				.collect(Collectors.toList()), ",");
	}

}
