package org.flowninja.persistence.mongodb.services;

import org.flowninja.persistence.mongodb.MongoConstants;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

@Configuration
@EnableMongoRepositories(basePackageClasses=MongoConstants.class, mongoTemplateRef="mongoTemplate")
@ComponentScan(basePackageClasses=MongoConstants.class) class MongoTestConfig {
	private MongodForTestsFactory mongoFactory;

	private MongoDbFactory dbFactory;
	private MongoTemplate mongoTemplate;
	
	public MongoTestConfig() throws Exception{
		this.mongoFactory = new MongodForTestsFactory(Version.Main.PRODUCTION);
	}
	
	@Bean
	public MongoDbFactory createMongoDbFactory() throws Exception {
		if(dbFactory == null)
			dbFactory = new SimpleMongoDbFactory(mongoFactory.newMongo(), "mongos");
		
		return dbFactory;
	}
	
	@Bean(name="mongoTemplate")
	public MongoTemplate createMongoTemplate() throws Exception {
		if(mongoTemplate == null)
			mongoTemplate = new MongoTemplate(createMongoDbFactory());
		
		return mongoTemplate;
	}
	
	@Bean
	public Object registerShutdownHook() {
		return new DisposableBean() {
			
			@Override
			public void destroy() throws Exception {
				mongoFactory.shutdown();
			}
		};
	}
}