package org.flowninja.common.kafka.serialization;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BsonObjectSerializer<T> implements Serializer<T> {

	private ObjectMapper objectMapper;
	private Class<T> serializedClass;

	public BsonObjectSerializer() {
		objectMapper = new ObjectMapper(new BsonFactory());
	}

	public BsonObjectSerializer(final Class<T> serializedClass) {
		objectMapper = new ObjectMapper(new BsonFactory());
		this.serializedClass = serializedClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		if (serializedClass == null) {
			final String propertyName = isKey ? "key.serialized.class" : "value.serialized.class";

			final String className = (String) configs.get(propertyName);

			if (StringUtils.isBlank(className)) {
				throw new IllegalStateException("class name property '" + propertyName + "' not set");
			}

			try {
				serializedClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				log.info("Cannot load deserialized class '{}'", className, e);

				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public byte[] serialize(final String topic, final T data) {
		if (data == null) {
			return null;
		}

		if (!serializedClass.isInstance(data)) {
			throw new RuntimeException("Cannot serialize value of type '" + data.getClass().getName()
					+ "' serializer is configured for class '" + serializedClass.getName() + "'");
		}

		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();

			objectMapper.writeValue(baos, data);

			baos.close();

			return baos.toByteArray();
		} catch (IOException e) {
			log.info("serialization failed", e);

			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
	}

}
