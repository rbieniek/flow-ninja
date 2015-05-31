/**
 * 
 */
package org.flowninja.statistics.redis.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.flowninja.statistics.generic.services.RpslStatisticsData;
import org.flowninja.statistics.redis.components.LongRedisSerializer;
import org.flowninja.statistics.redis.components.RPSLStatisticsKeyRedisSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RedisRpslStatisticsServiceTest.Config.class)
public class RedisRpslStatisticsServiceTest {

	@Configuration
	public static class Config {
		@Bean
		public RedisConnectionFactory connectionFactory() {
			SrpConnectionFactory factory = new SrpConnectionFactory();
			
			factory.setHostName("localhost");
			factory.setPort(16379);
			
			return factory;
		}

		@Bean
		public RedisSerializer<LocalDateTime> keySerializer() {
			return new RPSLStatisticsKeyRedisSerializer();
		}
		
		@Bean
		public RedisTemplate<LocalDateTime, Map<String, Long>> redisTemplate() {
			RedisTemplate<LocalDateTime, Map<String, Long>> template = new RedisTemplate<LocalDateTime, Map<String, Long>>();
			
			template.setConnectionFactory(connectionFactory());
			template.setKeySerializer(keySerializer());
			template.setHashKeySerializer(new StringRedisSerializer());
			template.setHashValueSerializer(new LongRedisSerializer());
			
			return template;
		}

		@Bean
		public RedisRpslStatisticsService service() {
			RedisRpslStatisticsService service = new RedisRpslStatisticsService();
			
			service.setTemplate(redisTemplate());
			
			return service;
		}
	}
	
	@Autowired
	RedisRpslStatisticsService service;
	
	@Autowired
	RedisTemplate<LocalDateTime, Map<String, Long>> redisTemplate;
	
	HashOperations<LocalDateTime, String, Long> ops;

	private LocalDateTime thisHour;

	private RedisServer embeddedServer;
	
	@Before
	public void before() {
		embeddedServer = (new RedisServerBuilder()).port(16379).build();
		
		embeddedServer.start();
		
		assertThat(embeddedServer.isActive());

		ops = redisTemplate.opsForHash();
		thisHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
	}
	
	@After
	public void after() {
		embeddedServer.stop();
	}

	@Test
	public void recordLookupRequest() {
		assertThat(ops.hasKey(thisHour, "lookupRequests")).isFalse();
		
		service.recordLookupRequest();
		
		assertThat(ops.get(thisHour, "lookupRequests")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
	}

	@Test
	public void badRequest() {
		assertThat(ops.hasKey(thisHour, "badRequest")).isFalse();
		
		service.recordBadRequest();
		
		assertThat(ops.get(thisHour, "badRequest")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 1L, 0L, 0L, 0L, 0L, 0L, 0L));
	}

	@Test
	public void notFound() {
		assertThat(ops.hasKey(thisHour, "notFound")).isFalse();
		
		service.recordNotFound();
		
		assertThat(ops.get(thisHour, "notFound")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 0L));
	}

	@Test
	public void administrativelyBlocked() {
		assertThat(ops.hasKey(thisHour, "adminstrativelyBlocked")).isFalse();
		
		service.recordAdminsitrativeBlocked();
		
		assertThat(ops.get(thisHour, "adminstrativelyBlocked")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L));
	}

	@Test
	public void resultsFromCache() {
		assertThat(ops.hasKey(thisHour, "resultsFromCache")).isFalse();
		
		service.recordResultFromCache();
		
		assertThat(ops.get(thisHour, "resultsFromCache")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 0L));
	}

	@Test
	public void resultsFromJsonService() {
		assertThat(ops.hasKey(thisHour, "resultsFromJsonService")).isFalse();
		
		service.recordResultFromJsonService();
		
		assertThat(ops.get(thisHour, "resultsFromJsonService")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 0L, 0L, 0L, 1L, 0L, 0L));
	}

	@Test
	public void resultsFromWhoisService() {
		assertThat(ops.hasKey(thisHour, "resultsFromWhoisService")).isFalse();
		
		service.recordResultFromWhoisService();
		
		assertThat(ops.get(thisHour, "resultsFromWhoisService")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 0L, 0L, 0L, 0L, 1L, 0L));
	}

	@Test
	public void resultsFromRdapService() {
		assertThat(ops.hasKey(thisHour, "resultsFromRdapService")).isFalse();
		
		service.recordResultFromRdapService();
		
		assertThat(ops.get(thisHour, "resultsFromRdapService")).isEqualTo(1);
		assertThat(service.history()).hasSize(720).contains(new RpslStatisticsData(thisHour, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 1L));
	}
}
