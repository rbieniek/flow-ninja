package org.flowninja.common.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.common.TestConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes=EmbeddedKafkaBrokerIntegrationTest.TestConfiguration.class)
public class EmbeddedKafkaBrokerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Autowired
    private EmbeddedKafkaBrokerProperties brokerProperties;

    @Test
    public void shouldSendMessageToBroker() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerProperties.getBindAddr().getHostAddress() + ":" + brokerProperties.getPortNumber());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<>("my-topic", Integer.toString(i), Integer.toString(i)));
        }

        producer.close();
    }

    @TestConfig
    @Import(EmbeddedKafkaTestConfiguration.class)
    public static class TestConfiguration {

    }
}
