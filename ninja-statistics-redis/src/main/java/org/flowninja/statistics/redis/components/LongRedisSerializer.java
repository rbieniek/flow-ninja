/**
 * 
 */
package org.flowninja.statistics.redis.components;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author rainer
 *
 */
public class LongRedisSerializer implements RedisSerializer<Long> {
	private static final Logger logger = LoggerFactory.getLogger(LongRedisSerializer.class);
	
	private Charset charset = Charset.forName("ASCII");
	
	@Override
	public byte[] serialize(Long t) throws SerializationException {
		return Long.toString(t).getBytes(charset);
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		try {
			if(bytes != null && bytes.length > 0)
				return Long.parseLong(new String(bytes, charset));
			else
				return 0L;
		} catch(NumberFormatException e) {
			logger.error("failed to parse value", e);
			
			throw new SerializationException("failed to parse value", e);
		}
	}

}
