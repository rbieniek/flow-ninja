package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.fest.assertions.data.MapEntry;
import org.flowninja.persistence.generic.types.IOAuth2AccessToken;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.IOAuth2RefreshToken;
import org.flowninja.persistence.generic.types.IOAuth2Request;
import org.flowninja.persistence.generic.types.impl.OAuth2AccessTokenImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2AuthenticationImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RefreshTokenImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RequestImpl;
import org.flowninja.persistence.mongodb.data.MongoOAuth2AccessToken;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.flowninja.persistence.mongodb.data.MongoOAuth2RefreshToken;
import org.flowninja.persistence.mongodb.repositories.MongoOAuth2AccessTokenRepository;
import org.flowninja.persistence.mongodb.repositories.MongoOAuth2AuthenticationRepository;
import org.flowninja.persistence.mongodb.repositories.MongoOAuth2RefreshTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=MongoTestConfig.class)
public class MongoOAuth2TokenPersistenceServiceTest {
	@Autowired
	private MongoOAuth2RefreshTokenPersistenceManager refreshTokenPersistence;
	
	@Autowired
	private MongoOAuth2AuthenticationPersistenceManager authenticationPersistence;

	@Autowired
	private MongoOAuth2TokenPersistenceService tokenStore;
	
	@Autowired
	private MongoOAuth2RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private MongoOAuth2AuthenticationRepository authenticationRepository;
	
	@Autowired
	private MongoOAuth2AccessTokenRepository accessTokenRepository;
	
	@Test
	public void persistBasicToken() {
		IOAuth2AccessToken accessToken = accessToken();
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).isNull();
		assertThat(mat.getAuthentication()).isNull();
		assertThat(mat.getExpiration()).isNull();
		assertThat(mat.getRefreshToken()).isNull();
		assertThat(mat.getScope()).isNull();
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void persistTokenWithSimpleValues() {
		IOAuth2AccessToken accessToken = accessToken();
		
		accessToken.setExpiration(new Date());
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).isNull();
		assertThat(mat.getAuthentication()).isNull();
		assertThat(mat.getExpiration()).isEqualTo(accessToken.getExpiration());
		assertThat(mat.getRefreshToken()).isNull();
		assertThat(mat.getScope()).isNull();
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void persistTokenWithAdditionalInformation() {
		IOAuth2AccessToken accessToken = accessToken();
		String infoOne = "foo";
		String infoTwo = "bar";

		accessToken.setAdditionalInformation(new HashMap<>());
		accessToken.getAdditionalInformation().put("one", infoOne);
		accessToken.getAdditionalInformation().put("two", infoTwo);
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).contains(MapEntry.entry("one", infoOne), MapEntry.entry("two", infoTwo)).hasSize(2);
		assertThat(mat.getAuthentication()).isNull();
		assertThat(mat.getExpiration()).isNull();
		assertThat(mat.getRefreshToken()).isNull();
		assertThat(mat.getScope()).isNull();
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void persistTokenWithRefreshToken() {
		IOAuth2AccessToken accessToken = accessToken();
		IOAuth2RefreshToken refreshToken = refreshToken();
		
		accessToken.setRefreshToken(refreshToken);
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).isNull();
		assertThat(mat.getAuthentication()).isNull();
		assertThat(mat.getExpiration()).isNull();
		assertThat(mat.getRefreshToken()).isNotNull();
		assertThat(mat.getRefreshToken().getValue()).isEqualTo(refreshToken.getValue());
		assertThat(mat.getScope()).isNull();
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void persistTokenWithAuthentication() {
		IOAuth2AccessToken accessToken = accessToken();
		IOAuth2Authentication authentication = basicAuthentication();
		
		accessToken.setAuthentication(authentication);
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).isNull();
		assertThat(mat.getAuthentication()).isNotNull();
		assertThat(mat.getAuthentication().getClientId()).isEqualTo(authentication.getStoredRequest().getClientId());
		assertThat(mat.getExpiration()).isNull();
		assertThat(mat.getRefreshToken()).isNull();
		assertThat(mat.getScope()).isNull();
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void persistTokenWithScope() {
		IOAuth2AccessToken accessToken = accessToken();
		
		accessToken.setScope(Sets.newHashSet("one", "two", "three"));
		
		MongoAccessTokenKey accessTokenKey = tokenStore.storeAccessToken(accessToken);

		assertThat(accessTokenKey).isNotNull();
		assertThat(accessTokenKey.getKeyValue()).isNotNull();
		
		MongoOAuth2AccessToken mat = accessTokenRepository.findOne(accessTokenKey.getKeyValue());
		
		assertThat(mat).isNotNull();
		assertThat(mat.getAdditionalInformation()).isNull();
		assertThat(mat.getAuthentication()).isNull();
		assertThat(mat.getExpiration()).isNull();
		assertThat(mat.getRefreshToken()).isNull();
		assertThat(mat.getScope()).containsOnly("one", "two", "three");
		assertThat(mat.getValue()).isNotNull();
	}

	@Test
	public void readAccessToken() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).isNull();
		assertThat(token.getAuthentication()).isNull();
		assertThat(token.getExpiration()).isNull();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getScope()).isNull();
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}

	@Test
	public void readAccessTokenWithAddtionalInformation() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		String dataOne = "alpha";
		String dataTwo = "beta";
		
		accessToken.setAdditionalInformation(new HashMap<String, Object>());
		accessToken.getAdditionalInformation().put("one", dataOne);
		accessToken.getAdditionalInformation().put("two", dataTwo);
		
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).contains(MapEntry.entry("one", dataOne), MapEntry.entry("two", dataTwo)).hasSize(2);
		assertThat(token.getAuthentication()).isNull();
		assertThat(token.getExpiration()).isNull();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getScope()).isNull();
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}

	@Test
	public void readAccessTokenWithAuthentication() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		MongoOAuth2Authentication authentication = basicMongoAuthentication();

		authenticationRepository.save(authentication);
		
		accessToken.setAuthentication(authentication);
		
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).isNull();
		assertThat(token.getAuthentication()).isInstanceOf(IOAuth2Authentication.class);
		assertThat(token.getAuthentication().getStoredRequest()).isInstanceOf(IOAuth2Request.class);
		assertThat(token.getAuthentication().getStoredRequest().getClientId()).isEqualTo(authentication.getClientId());
		assertThat(token.getExpiration()).isNull();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getScope()).isNull();
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}

	@Test
	public void readAccessTokenWithExpiration() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		
		accessToken.setExpiration(new Date());
		
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).isNull();
		assertThat(token.getAuthentication()).isNull();
		assertThat(token.getExpiration()).isEqualTo(accessToken.getExpiration());
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getScope()).isNull();
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}
	
	@Test
	public void readAccessTokenWithRefreshToken() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		MongoOAuth2RefreshToken refreshToken = new MongoOAuth2RefreshToken();
		
		refreshToken.setId(new ObjectId());
		refreshToken.setValue(UUID.randomUUID().toString());
		
		refreshTokenRepository.save(refreshToken);

		accessToken.setRefreshToken(refreshToken);
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).isNull();
		assertThat(token.getAuthentication()).isNull();
		assertThat(token.getExpiration()).isNull();
		assertThat(token.getRefreshToken()).isInstanceOf(IOAuth2RefreshToken.class);
		assertThat(token.getRefreshToken().getValue()).isEqualTo(refreshToken.getValue());
		assertThat(token.getScope()).isNull();
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}

	@Test
	public void readAccessTokenWithScope() {
		MongoOAuth2AccessToken accessToken = mongoAccessToken();
		
		accessToken.setScope(Lists.newArrayList("one", "two"));
		
		accessTokenRepository.save(accessToken);
		
		IOAuth2AccessToken token = tokenStore.readAccessTokenForValue(accessToken.getValue());
		
		assertThat(token).isNotNull();
		assertThat(token.getAdditionalInformation()).isNull();
		assertThat(token.getAuthentication()).isNull();
		assertThat(token.getExpiration()).isNull();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getScope()).containsOnly("one", "two");
		assertThat(token.getValue()).isEqualTo(accessToken.getValue());
	}

	private IOAuth2AccessToken accessToken() {
		IOAuth2AccessToken accessToken = new OAuth2AccessTokenImpl();

		accessToken.setValue(UUID.randomUUID().toString());
		
		return accessToken;
	}

	private MongoOAuth2AccessToken mongoAccessToken() {
		MongoOAuth2AccessToken accessToken = new MongoOAuth2AccessToken();

		accessToken.setId(new ObjectId());
		accessToken.setValue(UUID.randomUUID().toString());
		
		return accessToken;
	}

	private IOAuth2RefreshToken refreshToken() {
		IOAuth2RefreshToken refreshToken = new OAuth2RefreshTokenImpl();
		
		refreshToken.setValue(UUID.randomUUID().toString());
		
		return refreshToken;		
	}
	
	private IOAuth2Authentication basicAuthentication() {
		IOAuth2Authentication auth = new OAuth2AuthenticationImpl();
		IOAuth2Request request = new OAuth2RequestImpl();
		
		request.setApproved(true);
		request.setClientId("foo@bar.com");
		
		auth.setStoredRequest(request);
		
		return auth;
	}

	public MongoOAuth2Authentication basicMongoAuthentication() {
		MongoOAuth2Authentication auth = new MongoOAuth2Authentication();
		
		auth.setId(new ObjectId());
		auth.setApproved(true);
		auth.setClientId("foo@bar.com");
		
		return auth;
	}

}
