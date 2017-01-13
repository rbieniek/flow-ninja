package org.flowninja.common.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class BsonObjectSerDe implements Serde<Object> {

	private BsonObjectSerializer serializer;
	private BsonObjectDeserializer deserializer;

	public BsonObjectSerDe() {
		serializer = new BsonObjectSerializer();
		deserializer = new BsonObjectDeserializer();
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
	public Serializer<Object> serializer() {
		return serializer;
	}

	@Override
	public Deserializer<Object> deserializer() {
		return deserializer;
	}

}
