/**
 * 
 */
package org.flowninja.shell.admin.mongodb;

import java.net.UnknownHostException;

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
		return new MongoClient(new ServerAddress(env.getProperty("mongodb.host"), 
				env.getProperty("mongodb.port", Integer.class)));
	}
	
	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongoClient(), env.getProperty("mongodb.name"));
	}
	
	public @Bean(name="mongoTemplate") MongoTemplate mongoTemplate() throws Exception {
	    return new MongoTemplate(mongoDbFactory());
	}
}
