/**
 * 
 */
package org.flowninja.webapp.rspl.redis.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author rainer
 *
 */
@Configuration
public class PropertyAwareRedisConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(PropertyAwareRedisConfiguration.class);
	
	@Autowired
	private PropertyResolver propertyResolver;
	
	@Autowired(required=false)
	private JedisPoolConfig poolConfig;
	
	@Bean(name="jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = null;
		
		if(propertyResolver.getProperty("redis.master.nodes") != null && propertyResolver.getProperty("redis.master.name") != null) {
			RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();

			logger.info("Creating JedisConnectionFactory in sentinel mode: master={}", propertyResolver.getProperty("redis.master.name"));

			StringUtils.commaDelimitedListToSet(propertyResolver.getProperty("redis.master.nodes")).forEach((node) -> {
				logger.info("adding Redis node: {}", node);
				
				int idx = node.indexOf(':');
				
				if(idx > 0) {
					try {
						sentinelConfiguration.addSentinel(new RedisNode(node.substring(0, idx), Integer.parseInt(node.substring(idx+1))));
					} catch(NumberFormatException e) {
						logger.error("Invalid node specification passed: {}", node, e);
						
						throw new IllegalArgumentException("Invalid node specification passed: " + node, e);					
					}
				} else {
					logger.error("Invalid node specification passed: {}", node);

					throw new IllegalArgumentException("Invalid node specification passed: " + node);
				}
			});

			sentinelConfiguration.setMaster(propertyResolver.getProperty("redis.master.name"));

			factory = new JedisConnectionFactory(sentinelConfiguration, poolConfig);
		} else {
			String hostname = propertyResolver.getProperty("redis.host", "localhost");
			int port = propertyResolver.getProperty("redis.port", Integer.class, 6379);

			logger.info("Creating JedisConnectionFactory in single host mode: host={}, port={}", hostname, port);
			
			factory = new JedisConnectionFactory(poolConfig);
			
			factory.setHostName(hostname);
			factory.setPort(port);
		}
		
		return factory;
	}
}
