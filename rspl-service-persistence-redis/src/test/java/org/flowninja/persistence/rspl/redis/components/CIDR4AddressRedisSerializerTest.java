/**
 * 
 */
package org.flowninja.persistence.rspl.redis.components;

import static org.fest.assertions.api.Assertions.*;

import java.nio.charset.Charset;

import org.flowninja.types.net.CIDR4Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CIDR4AddressRedisSerializerTest.Config.class)
public class CIDR4AddressRedisSerializerTest {

	@Configuration
	public static class Config {
		
		@Bean
		public CIDR4AddressRedisSerializer serializer() {
			return new CIDR4AddressRedisSerializer();
		} 
	}
	
	@Autowired
	private CIDR4AddressRedisSerializer serializer;
	
	@Test
	public void serialize() {
		assertThat(serializer.serialize(new CIDR4Address(new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00}, 16)))
			.isEqualTo((new String("cidr:c0a80000/16")).getBytes(Charset.forName("ASCII")));
	}

	@Test
	public void deserialize() {
		assertThat(serializer.deserialize((new String("cidr:c0a80000/16")).getBytes(Charset.forName("ASCII"))))
			.isEqualTo(new CIDR4Address(new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00}, 16));
	}
	
	@Test(expected=SerializationException.class)
	public void deserializeNull() {
		serializer.deserialize(null);
	}
	
	@Test(expected=SerializationException.class)
	public void serializeNull() {
		serializer.serialize(null);
	}
}
