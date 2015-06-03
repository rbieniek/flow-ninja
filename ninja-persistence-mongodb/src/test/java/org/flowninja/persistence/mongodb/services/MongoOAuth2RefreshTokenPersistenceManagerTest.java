package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;
import java.util.UUID;

import org.flowninja.persistence.generic.types.IOAuth2RefreshToken;
import org.flowninja.persistence.generic.types.impl.OAuth2RefreshTokenImpl;
import org.flowninja.persistence.mongodb.MongoConstants;
import org.flowninja.persistence.mongodb.data.MongoOAuth2RefreshToken;
import org.flowninja.persistence.mongodb.repositories.IMongoOAuth2RefreshTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=MongoTestConfig.class)
public class MongoOAuth2RefreshTokenPersistenceManagerTest {
	@Autowired
	private MongoOAuth2RefreshTokenPersistenceManager persistence;
	
	@Autowired
	private IMongoOAuth2RefreshTokenRepository repository;
	
	@Test
	public void persistRefreshToken() {
		IOAuth2RefreshToken token = new OAuth2RefreshTokenImpl();
		
		token.setValue("foo_bar");
		
		MongoOAuth2RefreshToken rt = persistence.persistRefreshToken(token, null);
		
		assertThat(rt).isNotNull();
		
		MongoOAuth2RefreshToken prt = repository.findOne(rt.getId());

		assertThat(prt).isNotNull();
		assertThat(prt.getValue()).isEqualTo("foo_bar");
		assertThat(prt.getExpiration()).isNull();
	}

	@Test
	public void persistRefreshTokenWithExpiration() {
		IOAuth2RefreshToken token = new OAuth2RefreshTokenImpl();
		
		token.setValue("foo_bar");
		token.setExpiration(new Date());
		
		MongoOAuth2RefreshToken rt = persistence.persistRefreshToken(token, null);
		
		assertThat(rt).isNotNull();
		
		MongoOAuth2RefreshToken prt = repository.findOne(rt.getId());

		assertThat(prt).isNotNull();
		assertThat(prt.getValue()).isEqualTo("foo_bar");
		assertThat(prt.getExpiration()).isEqualTo(token.getExpiration());
	}
	
	@Test
	public void restoreRefreshToken() {
		MongoOAuth2RefreshToken token = new MongoOAuth2RefreshToken();
		
		token.setValue(UUID.randomUUID().toString());
		
		IOAuth2RefreshToken wt = persistence.restoreRefreshToken(token);
		
		assertThat(wt).isNotNull();
		assertThat(wt.getExpiration()).isNull();
		assertThat(wt.getValue()).isEqualTo(token.getValue());
	}
	
	@Test
	public void restoreExpiringRefreshToken() {
		MongoOAuth2RefreshToken token = new MongoOAuth2RefreshToken();
		
		token.setValue(UUID.randomUUID().toString());
		token.setExpiration(new Date());
		
		IOAuth2RefreshToken wt = persistence.restoreRefreshToken(token);
		
		assertThat(wt).isNotNull();
		assertThat(wt.getExpiration()).isEqualTo(token.getExpiration());
		assertThat(wt.getValue()).isEqualTo(token.getValue());
	}
}
