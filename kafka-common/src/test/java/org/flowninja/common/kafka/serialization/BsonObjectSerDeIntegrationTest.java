package org.flowninja.common.kafka.serialization;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.common.TestConfig;
import org.flowninja.common.kafka.EmbeddedKafkaBroker;
import org.flowninja.common.kafka.EmbeddedKafkaBrokerProperties;
import org.flowninja.common.kafka.EmbeddedKafkaTestConfiguration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BsonObjectSerDeIntegrationTest.TestConfiguration.class)
@Slf4j
public class BsonObjectSerDeIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Autowired
    private EmbeddedKafkaBrokerProperties brokerProperties;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldSendMessagesToBroker() throws Exception {
        final Properties props = new Properties();

        props.put("bootstrap.servers", brokerProperties.brokerAddress());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        final Producer<Integer, TestPayload> producer = new KafkaProducer<>(props, new IntegerSerializer(),
                new BsonObjectSerializer(TestPayload.class));

        for (int i = 0; i < 10; i++) {
            final RecordMetadata rmd = producer.send(new ProducerRecord<>("write-only-topic", i,
                    TestPayload.builder().largeNumber(BigInteger.valueOf(i)).name("value#" + i).number(i).build())).get();

            assertThat(rmd).isNotNull();

            log.info("Wrote record with key {} into topic {} with checksum {} at partition {} offet {} at timestamp {}", i, rmd.topic(), rmd.checksum(), rmd.partition(), rmd.offset(), rmd.timestamp());
        }
        producer.flush();
        producer.close();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldSendAndReadMessagesToBroker() throws Exception {
        final Map<Integer, TestPayload> payloads = new HashMap<>();

        final Properties producerProps = new Properties();

        producerProps.put("bootstrap.servers", brokerProperties.brokerAddress());
        producerProps.put("acks", "all");
        producerProps.put("retries", 0);
        producerProps.put("batch.size", 16384);
        producerProps.put("linger.ms", 1);
        producerProps.put("buffer.memory", 33554432);

        final Producer<Integer, TestPayload> producer = new KafkaProducer<>(producerProps, new IntegerSerializer(),
                new BsonObjectSerializer(TestPayload.class));

        for (int i = 0; i < 10; i++) {
            final TestPayload payload = TestPayload.builder().largeNumber(BigInteger.valueOf(i)).name("value#" + i)
                    .number(i).build();
            final RecordMetadata rmd =  producer.send(new ProducerRecord<>("read-write-topic", i, payload)).get();

            assertThat(rmd).isNotNull();

            log.info("Wrote record with key {} into topic {} with checksum {} at partition {} offet {} at timestamp {}", i, rmd.topic(), rmd.checksum(), rmd.partition(), rmd.offset(), rmd.timestamp());

            payloads.put(i, payload);
        }

        producer.flush();
        producer.close();

        final Properties consumerProps = new Properties();

        consumerProps.put("bootstrap.servers", brokerProperties.brokerAddress());
        consumerProps.put("group.id", "read-write-test");
        consumerProps.put("enable.auto.commit", "true");
        consumerProps.put("auto.commit.interval.ms", "1000");

        final Consumer<Integer, TestPayload> consumer = new KafkaConsumer<>(consumerProps, new IntegerDeserializer(),
                new BsonObjectDeserializer<>(TestPayload.class));

        consumer.subscribe(Arrays.asList("read-write-topic"), new ConsumerRebalanceListener() {

            @Override
            public void onPartitionsRevoked(final Collection<TopicPartition> partitions) {
                for(final TopicPartition part : partitions) {
                    log.info("consumer lost access to to topic {} and paritition {}", part.topic(), part.partition());
                }
            }

            @Override
            public void onPartitionsAssigned(final Collection<TopicPartition> partitions) {
                for(final TopicPartition part : partitions) {
                    log.info("consumer gained access to to topic {} and paritition {}", part.topic(), part.partition());
                }

                // Circumvent bug in Kafka which causes read index on behind max message on auto-created topics
                consumer.seekToBeginning(partitions);
            }
        });


        while (!payloads.isEmpty()) {
            final ConsumerRecords<Integer, TestPayload> records = consumer.poll(100);

            log.info("Retrieved {} records from broker", records.count());

            for (ConsumerRecord<Integer, TestPayload> record : records) {
                final Integer key = record.key();
                final TestPayload value = record.value();

                log.info("Retrieved consumer record key {} value {}", key, value);

                assertThat(payloads).containsKey(key);
                assertThat(payloads.remove(key)).isEqualTo(value);
            }
        }

        consumer.close();
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class TestPayload {
        private int number;
        private String name;
        private BigInteger largeNumber;
    }

    @TestConfig
    @Import(EmbeddedKafkaTestConfiguration.class)
    public static class TestConfiguration {

    }
}
