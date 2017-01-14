package org.flowninja.common.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class BsonObjectSerDe<T> implements Serde<T> {

    private BsonObjectSerializer<T> serializer;
    private BsonObjectDeserializer<T> deserializer;

    public BsonObjectSerDe() {
        serializer = new BsonObjectSerializer<>();
        deserializer = new BsonObjectDeserializer<>();
    }

    public BsonObjectSerDe(final Class<T> clazz) {
        serializer = new BsonObjectSerializer<>(clazz);
        deserializer = new BsonObjectDeserializer<>(clazz);
    }

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }

}
