package org.flowninja.common.kafka.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BsonObjectDeserializer<T> implements Deserializer<T> {

	private ObjectMapper objectMapper;
	private Class<?> deserializedClass;

	public BsonObjectDeserializer() {
		objectMapper = new ObjectMapper(new BsonFactory());
	}

	public BsonObjectDeserializer(final Class<T> deserializedClass) {
		objectMapper = new ObjectMapper(new BsonFactory());
		this.deserializedClass = deserializedClass;
	}

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		if (deserializedClass == null) {
			final String propertyName = isKey ? "key.serialized.class" : "value.serialized.class";

			final String className = (String) configs.get(propertyName);

			if (StringUtils.isBlank(className)) {
				throw new IllegalStateException("class name property '" + propertyName + "' not set");
			}

			try {
				deserializedClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				log.info("Cannot load deserialized class '{}'", className, e);

				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(final String topic, final byte[] data) {
		if (data == null) {
			return null;
		}

		final ByteArrayInputStream bais = new ByteArrayInputStream(data);

		try {
			return (T) objectMapper.readValue(bais, deserializedClass);
		} catch (IOException e) {
			log.info("deserialization failed", e);

			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
	}

}
