/**
 * 
 */
package org.flowninja.webapp.collector.mongodb.components;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * @author rainer
 *
 */
@Configuration
public class PropertyAwareMongoDBConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(PropertyAwareMongoDBConfiguration.class);
	
	@Autowired
	private Environment properties;

	@Autowired(required=false)
	private MongoClientOptions clientOptions;
	
	@Bean(name="mongo")
	public MongoClient mongo() {
		List<MongoCredential> credentials = null;
		
		logger.info("MongoDB replicas='{}', host='{}', port='{}', database name='{}', user='{}', password='{}'", 
				properties.getProperty("mongodb.replicas"), 
				properties.getProperty("mongodb.host"), properties.getProperty("mongodb.port"),
				properties.getProperty("mongodb.name"), 
				properties.getProperty("mongodb.user"), properties.getProperty("mongodb.password"));

		if(properties.getProperty("mongodb.user") != null && properties.getProperty("mongodb.password") != null) {
			credentials = new LinkedList<MongoCredential>();
			
			credentials.add(MongoCredential.createCredential(properties.getProperty("mongodb.user"), 
					properties.getProperty("mongodb.name", "db"), 
					properties.getProperty("mongodb.password").toCharArray()));
		}

		MongoClientOptions.Builder builder;
		
		if(clientOptions != null)
			builder = MongoClientOptions.builder(clientOptions);
		else 
			builder = MongoClientOptions.builder();

		if(properties.getProperty("mongodb.replicas") != null) {
			List<ServerAddress> serverAddresses = StringUtils.commaDelimitedListToSet(properties.getProperty("mongodb.replicas")).stream().map((node) -> {
				logger.info("adding MongoDB node: {}", node);
				
				int idx = node.indexOf(':');
				
				if(idx > 0) {
					try {
						return new ServerAddress(node.substring(0, idx), Integer.parseInt(node.substring(idx+1)));
					} catch(NumberFormatException e) {
						logger.error("Invalid node specification passed: {}", node, e);
						
						throw new IllegalArgumentException("Invalid node specification passed: " + node, e);					
					}
				} else {
					logger.error("Invalid node specification passed: {}", node);

					throw new IllegalArgumentException("Invalid node specification passed: " + node);
				}
			}).collect(Collectors.toList()); 
			
			if(properties.getProperty("mongodb.setname") != null)
				builder.requiredReplicaSetName(properties.getProperty("mongodb.setname"));
			
			if(credentials != null)
				return new MongoClient(serverAddresses, credentials, builder.build());
			else
				return new MongoClient(serverAddresses, builder.build());
		} else {
			if(credentials != null)
				return new MongoClient(new ServerAddress(properties.getProperty("mongodb.host", "localhost"), 
						properties.getProperty("mongodb.port", Integer.class, 27017)), credentials, builder.build());
			else
				return new MongoClient(new ServerAddress(properties.getProperty("mongodb.host", "localhost"), 
						properties.getProperty("mongodb.port", Integer.class, 27017)), builder.build());
		}
	}
	
	@Bean(name="mongoDbFactory")
	public MongoDbFactory dbFactory() {
		return new SimpleMongoDbFactory(mongo(), properties.getProperty("mongodb.name", "db"));
	}
}
