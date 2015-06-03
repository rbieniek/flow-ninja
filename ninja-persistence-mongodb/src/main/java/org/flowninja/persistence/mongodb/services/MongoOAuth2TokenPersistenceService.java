/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bson.types.ObjectId;
import org.flowninja.persistence.generic.services.IOAuth2TokenPersistenceService;
import org.flowninja.persistence.generic.types.IOAuth2AccessToken;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.impl.OAuth2AccessTokenImpl;
import org.flowninja.persistence.mongodb.data.MongoOAuth2AccessToken;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.flowninja.persistence.mongodb.data.QMongoOAuth2AccessToken;
import org.flowninja.persistence.mongodb.repositories.IMongoOAuth2AccessTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class MongoOAuth2TokenPersistenceService implements IOAuth2TokenPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoOAuth2TokenPersistenceService.class);
	
	@Autowired
	private MongoOAuth2AuthenticationPersistenceManager authenticationPersistence;

	@Autowired
	private MongoOAuth2RefreshTokenPersistenceManager refreshTokenPersistence; 
	
	@Autowired
	private IMongoOAuth2AccessTokenRepository accessTokenRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public MongoAccessTokenKey storeAccessToken(IOAuth2AccessToken wapiToken) {
		MongoOAuth2AccessToken accessToken = new MongoOAuth2AccessToken();
		
		logger.info("persisting OAuth2 access token: {}", wapiToken);
		
		accessToken.setId(new ObjectId());
		if(isNotEmpty(wapiToken.getAdditionalInformation()))
			accessToken.setAdditionalInformation(new HashMap<>(wapiToken.getAdditionalInformation()));
		accessToken.setExpiration(wapiToken.getExpiration());
		if(isNotEmpty(wapiToken.getScope()))
			accessToken.setScope(new ArrayList<>(wapiToken.getScope()));
		accessToken.setValue(wapiToken.getValue());

		MongoOAuth2Authentication mongoAuth2Authentication = null;
		
		if(wapiToken.getAuthentication() != null) {
			mongoAuth2Authentication = authenticationPersistence.persistWapiAuthentication(wapiToken.getAuthentication());
			
			accessToken.setAuthentication(mongoAuth2Authentication);
		}
		if(wapiToken.getRefreshToken() != null) {
			accessToken.setRefreshToken(refreshTokenPersistence.persistRefreshToken(wapiToken.getRefreshToken(), mongoAuth2Authentication));
		}
		
		accessTokenRepository.save(accessToken);
		
		logger.info("persisted mongodb access token: {}", accessToken);

		return new MongoAccessTokenKey(accessToken.getId());
	}

	private static final boolean isNotEmpty(Collection<?> col) {
		return (col != null && !col.isEmpty());
	}

	private static final boolean isNotEmpty(Map<?,?> col) {
		return (col != null && !col.isEmpty());
	}

	@Override
	public IOAuth2AccessToken readAccessTokenForValue(String value) {
		IOAuth2AccessToken wapiToken = null;

		logger.info("reading access token for value {}", value);

		MongoOAuth2AccessToken token =  accessTokenRepository.findOne(QMongoOAuth2AccessToken.mongoOAuth2AccessToken.value.eq(value));
		
		if(token != null) {
			wapiToken = new OAuth2AccessTokenImpl();

			if(isNotEmpty(token.getAdditionalInformation()))
				wapiToken.setAdditionalInformation(token.getAdditionalInformation());
			wapiToken.setExpiration(token.getExpiration());
			if(isNotEmpty(token.getScope()))
				wapiToken.setScope(new HashSet<>(token.getScope()));
			wapiToken.setValue(token.getValue());
			
			if(token.getAuthentication() != null)
				wapiToken.setAuthentication(authenticationPersistence.restoreAuthentication(token.getAuthentication()));
			if(token.getRefreshToken() != null)
				wapiToken.setRefreshToken(refreshTokenPersistence.restoreRefreshToken(token.getRefreshToken()));
		}
		
		logger.info("returning access token {} for value {}", value);
		
		return wapiToken;
	}

	@Override
	public IOAuth2Authentication readAuthenticationForAccessToken(String value) {
		IOAuth2Authentication result = null;
		IOAuth2AccessToken accessToken = readAccessTokenForValue(value);
		
		if(accessToken != null)
			result = accessToken.getAuthentication();
		
		return result;
	}

	@Override
	public void removeAccessToken(String value) {
		logger.info("remoing access token for value {}", value);

		MongoOAuth2AccessToken token =  accessTokenRepository.findOne(QMongoOAuth2AccessToken.mongoOAuth2AccessToken.value.eq(value));

		if(token != null) {
			logger.info("removing access token {}", token);
			
			if(token.getAuthentication() != null && token.getRefreshToken() == null) 
				authenticationPersistence.removeAuthentication(token.getAuthentication());
			
			accessTokenRepository.delete(token);
		}
	}
}
