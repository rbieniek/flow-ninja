/**
 * 
 */
package org.flowninja.persistence.rspl.redis.services;

import static org.fest.assertions.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.flowninja.persistence.rspl.generic.types.NetworkInformation;
import org.flowninja.persistence.rspl.redis.components.CIDR4AddressRedisSerializer;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.types.net.CIDR4Address;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RedisRsplPersistenceServiceTest.Config.class)
public class RedisRsplPersistenceServiceTest {

	@Configuration
	public static class Config {
		@Bean
		public CIDR4AddressRedisSerializer keySerializer() {
			return new CIDR4AddressRedisSerializer();
		}

		@Bean
		public Jackson2JsonRedisSerializer<NetworkInformation> valueSerializer() {
			return new Jackson2JsonRedisSerializer<NetworkInformation>(NetworkInformation.class);
		}
		
		@Bean
		public RedisConnectionFactory connectionFactory() {
			/*
			JedisConnectionFactory factory = new JedisConnectionFactory();
			
			factory.setHostName("localhost");
			factory.setPort(16379);
			factory.setUsePool(true);
			
			return factory;
			*/
			SrpConnectionFactory factory = new SrpConnectionFactory();
			
			factory.setHostName("localhost");
			factory.setPort(16379);
			
			return factory;
		}
		
		@Bean
		public RedisTemplate<CIDR4Address, NetworkInformation> redisTemplate() {
			RedisTemplate<CIDR4Address, NetworkInformation> template = new RedisTemplate<CIDR4Address, NetworkInformation>();
			
			template.setConnectionFactory(connectionFactory());
			template.setKeySerializer(keySerializer());
			template.setValueSerializer(valueSerializer());
			
			return template;
		}
		
		@Bean
		public RedisRsplPersistenceService service() {
			RedisRsplPersistenceService service = new RedisRsplPersistenceService();

			service.setMasterTemplate(redisTemplate());
			service.setSlaveTemplate(redisTemplate());
			service.setRetainTime(5);
			service.setRetainUnit(TimeUnit.SECONDS);
			
			return service;
		}
		
	}
	
	@Autowired
	private RedisRsplPersistenceService service;
	
	@Autowired
	private RedisTemplate<CIDR4Address, NetworkInformation> template;
	
	private RedisServer embeddedServer;
	
	@Before
	public void before() {
		embeddedServer = (new RedisServerBuilder()).port(16379).build();
		
		embeddedServer.start();
		
		assertThat(embeddedServer.isActive());
	}
	
	@After
	public void after() {
		embeddedServer.stop();
	}
	
	@Test
	public void netNotFound() {
		assertThat(service.loadNetworkInformation(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00}, 16)))
			.isNull();;
	}
	
	@Test
	public void insertNet() {
		service.persistNetworkInformation(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa9, (byte)0x00, (byte)0x00}, 16), 
			new NetworkInformation("foo", "bar", ENetworkRegistry.RIPE));

		assertThat(template.opsForValue().get(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa9, (byte)0x00, (byte)0x00}, 16)))
			.isEqualTo(new NetworkInformation("foo", "bar", ENetworkRegistry.RIPE));
		/*
		assertThat(service.loadNetworkInformation(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa9, (byte)0x00, (byte)0x00}, 16)))
			.isEqualTo(new NetworkInformation("foo", "bar", ENetworkRegistry.RIPE));
		 */		
	}

	@Test
	public void netFound() {
		template.opsForValue().set(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xaa, (byte)0x00, (byte)0x00}, 16), 
				new NetworkInformation("goo", "moo", ENetworkRegistry.AFRINIC));

		assertThat(service.loadNetworkInformation(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xaa, (byte)0x00, (byte)0x00}, 16)))
			.isEqualTo(new NetworkInformation("goo", "moo", ENetworkRegistry.AFRINIC));
	}
}
