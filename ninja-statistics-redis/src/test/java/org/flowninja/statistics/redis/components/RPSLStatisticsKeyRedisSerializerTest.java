/**
 * 
 */
package org.flowninja.statistics.redis.components;

import static org.fest.assertions.api.Assertions.*;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author rainer
 *
 */
public class RPSLStatisticsKeyRedisSerializerTest {

	private RPSLStatisticsKeyRedisSerializer serializer = new RPSLStatisticsKeyRedisSerializer();
	private Charset charset = Charset.forName("ASCII");
	
	@Test
	public void serializeKey() {
		LocalDateTime now = LocalDateTime.now();
		String key = String.format("statistics:rpsl:%04d-%02d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour());

		assertThat(serializer.serialize(LocalDateTime.now())).isEqualTo(key.getBytes(charset));
	}
	
	@Test
	public void deserializeKey() {
		LocalDateTime now = LocalDateTime.now();
		String key = String.format("statistics:rpsl:%04d-%02d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour());
		
		assertThat(serializer.deserialize(key.getBytes(charset))).isEqualTo(now.truncatedTo(ChronoUnit.HOURS));
	}

	@Test(expected=SerializationException.class)
	public void missingPrefix() {
		LocalDateTime now = LocalDateTime.now();
		String key = String.format("%04d-%02d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour());
		
		serializer.deserialize(key.getBytes(charset));
	}

	@Test(expected=SerializationException.class)
	public void badSeperators() {
		LocalDateTime now = LocalDateTime.now();
		String key = String.format("statistics:rpsl:%04d+%02d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour());
		
		serializer.deserialize(key.getBytes(charset));
	}

	@Test(expected=SerializationException.class)
	public void missingPart() {
		LocalDateTime now = LocalDateTime.now();
		String key = String.format("statistics:rpsl:%04d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		
		serializer.deserialize(key.getBytes(charset));
	}
}
