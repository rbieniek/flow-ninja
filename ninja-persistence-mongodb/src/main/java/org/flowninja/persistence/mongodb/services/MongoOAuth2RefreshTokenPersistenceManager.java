/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import org.bson.types.ObjectId;
import org.flowninja.persistence.generic.types.IOAuth2RefreshToken;
import org.flowninja.persistence.generic.types.impl.OAuth2RefreshTokenImpl;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.flowninja.persistence.mongodb.data.MongoOAuth2RefreshToken;
import org.flowninja.persistence.mongodb.repositories.MongoOAuth2RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class MongoOAuth2RefreshTokenPersistenceManager {

	@Autowired
	private MongoOAuth2RefreshTokenRepository repository;
	
	public MongoOAuth2RefreshToken persistRefreshToken(IOAuth2RefreshToken refreshToken, MongoOAuth2Authentication authentication) {
		MongoOAuth2RefreshToken mt = new MongoOAuth2RefreshToken();
		
		mt.setId(new ObjectId());
		mt.setValue(refreshToken.getValue());
		mt.setExpiration(refreshToken.getExpiration());
		if(authentication != null)
			mt.setAuthentication(authentication);
		
		repository.save(mt);
		
		return mt;
	}

	public IOAuth2RefreshToken restoreRefreshToken(MongoOAuth2RefreshToken refreshToken) {
		IOAuth2RefreshToken wapiToken = new OAuth2RefreshTokenImpl();
		
		wapiToken.setExpiration(refreshToken.getExpiration());
		wapiToken.setValue(refreshToken.getValue());
		
		return wapiToken;
	}
}
