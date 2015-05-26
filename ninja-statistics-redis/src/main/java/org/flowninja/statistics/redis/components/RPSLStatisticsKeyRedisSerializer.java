/**
 * 
 */
package org.flowninja.statistics.redis.components;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author rainer
 *
 */
public class RPSLStatisticsKeyRedisSerializer implements RedisSerializer<LocalDateTime> {
	private static final Logger logger = LoggerFactory.getLogger(RPSLStatisticsKeyRedisSerializer.class);
	
	private static final String KEY_PREFIX="statistics:rpsl:";
	
	private Charset charset = Charset.forName("ASCII");
	
	@Override
	public byte[] serialize(LocalDateTime t) throws SerializationException {
		StringBuilder builder = new StringBuilder(KEY_PREFIX);
		
		builder.append(String.format("%04d-%02d-%02d-%02d", 
				t.getYear(), t.getMonthValue(), t.getDayOfMonth(), t.getHour()));
		
		return builder.toString().getBytes(charset);
	}

	@Override
	public LocalDateTime deserialize(byte[] bytes) throws SerializationException {
		String key = null;

		try {
			key = new String(bytes, charset); 
		} catch(Exception e) {
			throw new SerializationException("cannot convert key to string", e);
		}
		
		if(!StringUtils.startsWith(key, KEY_PREFIX)) {
			logger.error("missing prefix on key: {}", key);
			
			throw new SerializationException("missing prefix on key: " + key);
		}
		
		String[] parts = StringUtils.split(StringUtils.substring(key, KEY_PREFIX.length()), "-");
		
		if(parts.length != 4) {
			logger.error("malformed key: {}, expected 4 parts and got {}", key, parts.length);

			throw new SerializationException("malformed key: " + key);
		}

		try {
			return LocalDateTime.of(LocalDate.of(Integer.parseInt(parts[0]), 
						Integer.parseInt(parts[1]), 
						Integer.parseInt(parts[2])), 
					LocalTime.of(Integer.parseInt(parts[3]), 0));
		} catch(NumberFormatException e) {
			logger.error("cannot parse time fields on key: {}", key);
			
			throw new SerializationException("cannot parse time fields on key: " + key);			
		}
	}

}
