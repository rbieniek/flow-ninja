/**
 * 
 */
package org.flowninja.shell.admin.mongodb;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import org.flowninja.persistence.mongodb.MongoConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author rainer
 *
 */
@Configuration
@EnableMongoRepositories(basePackageClasses=MongoConstants.class, mongoTemplateRef="mongoTemplate")
@PropertySource("classpath:org/flowninja/shell/admin/mongodb/mongodb.properties")
@ComponentScan(basePackageClasses=MongoConstants.class)
public class ShellConfiguration {
	@Autowired
	private Environment env;
	
	public @Bean MongoClient mongoClient() throws UnknownHostException {
		List<ServerAddress> replicas = StringUtils.commaDelimitedListToSet(env.getProperty("mongodb.replicas")).stream().map((addrSpec)-> {
			int idx = addrSpec.indexOf(':');
			
			if(idx > 0) {
				try {
					return new ServerAddress(addrSpec.substring(0, idx), Integer.parseInt(addrSpec.substring(idx+1)));
				} catch(NumberFormatException e) {
					throw new IllegalArgumentException("cannot parse replica address: " + addrSpec, e);					
				}
			} else {
				throw new IllegalArgumentException("cannot parse replica address: " + addrSpec);
			}
		}).collect(Collectors.toList());
		
		return new MongoClient(replicas);
	}
	
	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongoClient(), env.getProperty("mongodb.name"));
	}
	
	public @Bean(name="mongoTemplate") MongoTemplate mongoTemplate() throws Exception {
	    return new MongoTemplate(mongoDbFactory());
	}
}
