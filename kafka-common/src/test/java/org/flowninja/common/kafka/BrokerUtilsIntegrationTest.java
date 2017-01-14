package org.flowninja.common.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Optional;

import org.flowninja.common.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BrokerUtilsIntegrationTest.TestConfiguration.class)
public class BrokerUtilsIntegrationTest {

	@Autowired
	private BrokerUtils brokerUtils;

	@Autowired
	private KafkaBrokerHostProperties kafkaBrokerHostProperties;

	@Autowired
	private ZookeeperHostProperties zookeeperHostProperties;

	@Test
	public void shouldHaveBootstrapServers() {
		assertThat(brokerUtils.bootstrapServers())
				.isEqualTo("PLAINTEXT://" + kafkaBrokerHostProperties.brokerAddress());
	}

	@Test
	public void shouldHaveZookeeperServers() {
		assertThat(brokerUtils.zookeeperServers()).isEqualTo(zookeeperHostProperties.getBindAddr().getHostAddress()
				+ ":" + Integer.toString(zookeeperHostProperties.getPortNumber()));
	}

	@Test
	public void shouldCreateTopic() {
		brokerUtils.createTopic("test-topic", Optional.empty(), Optional.empty(), Optional.empty());
	}

	@TestConfig
	public static class TestConfiguration {
		@Bean
		@Autowired
		public BrokerUtils brokerUtils(final ZookeeperClusterProperties zookeeperClusterProperties,
				final KafkaBrokerClusterProperties kafkaBrokerClusterProperties) {
			return new BrokerUtils(zookeeperClusterProperties, kafkaBrokerClusterProperties);
		}

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
		public EmbeddedKafkaBroker embeddedKafkaBroker(final KafkaBrokerHostProperties kafkaProperties,
				final ZookeeperHostProperties zookeeperProperties) {
			return new EmbeddedKafkaBroker(kafkaProperties, zookeeperProperties);
		}

		@Bean
		@Autowired
		public EmbeddedZookeeper embeddedZookeeper(final ZookeeperHostProperties zookeeperProperties) {
			return new EmbeddedZookeeper(zookeeperProperties);
		}

		@Bean
		@Autowired
		public KafkaBrokerClusterProperties kafkaBrokerClusterProperties(
				final KafkaBrokerHostProperties kafkaBrokerHostProperties) {
			final KafkaBrokerClusterProperties kafkaBrokerClusterProperties = new KafkaBrokerClusterProperties();

			kafkaBrokerClusterProperties.setBrokers(Collections.singletonList(kafkaBrokerHostProperties));

			return kafkaBrokerClusterProperties;
		}

		@Bean
		@Autowired
		public ZookeeperClusterProperties zookeeperClusterProperties(
				final ZookeeperHostProperties zookeeperHostProperties) {
			final ZookeeperClusterProperties zookeeperClusterProperties = new ZookeeperClusterProperties();

			zookeeperClusterProperties.setServers(Collections.singletonList(zookeeperHostProperties));

			return zookeeperClusterProperties;
		}

	}
}
