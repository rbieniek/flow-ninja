/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.IOAuth2Request;
import org.flowninja.persistence.generic.types.impl.OAuth2AuthenticationImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RequestImpl;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.flowninja.persistence.mongodb.repositories.IMongoOAuth2AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;


/**
 * @author rainer
 *
 */
@Component
public class MongoOAuth2AuthenticationPersistenceManager {
	private static final Logger logger = LoggerFactory.getLogger(MongoOAuth2AuthenticationPersistenceManager.class);
	
	@Autowired
	private IMongoOAuth2AuthenticationRepository authenticationRepository;

	public MongoOAuth2Authentication persistWapiAuthentication(IOAuth2Authentication authentication) {
		MongoOAuth2Authentication ma = new MongoOAuth2Authentication();
		IOAuth2Request request = authentication.getStoredRequest();
		
		logger.info("Persisting authentication {}", authentication);
		
		ma.setId(new ObjectId());
		ma.setApproved(request.isApproved());
		ma.setClientId(request.getClientId());
		if(isNotEmpty(request.getExtensions())) {
			ma.setExtensions(new HashMap<>(Maps.transformEntries(request.getExtensions(), (k,v) -> new Binary(v))));
		}
		if(isNotEmpty(request.getGrantedAuthorities())) {
			ma.setGrantedAuthorities(new ArrayList<>(request.getGrantedAuthorities()));
		}
		ma.setRedirectUri(request.getRedirectUri());
		if(isNotEmpty(request.getRequestParameters())) {
			ma.setRequestParameters(new HashMap<>(request.getRequestParameters()));			
		}
		if(isNotEmpty(request.getResourceIds())) {
			ma.setResourceIds(new ArrayList<>(request.getResourceIds()));
		}
		if(isNotEmpty(request.getResponseTypes())) {
			ma.setResponseTypes(new ArrayList<>(request.getResponseTypes()));
		}
		if(isNotEmpty(request.getScope())) {
			ma.setScope(new ArrayList<>(request.getScope()));
		}
		if(authentication.getAuthentication() != null) {
			ma.setUserAuthentication(new Binary(authentication.getAuthentication()));
		}
		
		authenticationRepository.save(ma);

		logger.info("Persisted authentication {} to entit {}", authentication, ma);
		
		return ma;
	}

	private static final boolean isNotEmpty(Collection<?> col) {
		return (col != null && !col.isEmpty());
	}

	private static final boolean isNotEmpty(Map<?,?> col) {
		return (col != null && !col.isEmpty());
	}

	public IOAuth2Authentication restoreAuthentication(MongoOAuth2Authentication authentication) {
		IOAuth2Authentication wapiAuthentication = new OAuth2AuthenticationImpl();
		IOAuth2Request wapiRequest = new OAuth2RequestImpl();

		if(authentication.getUserAuthentication() != null)
			wapiAuthentication.setAuthentication(authentication.getUserAuthentication().getData());
		
		wapiRequest.setApproved(authentication.isApproved());
		wapiRequest.setClientId(authentication.getClientId());
		if(authentication.getExtensions() != null)
			wapiRequest.setExtensions(Maps.transformEntries(authentication.getExtensions(), (k, v) -> v.getData()));
		if(isNotEmpty(authentication.getGrantedAuthorities()))
			wapiRequest.setGrantedAuthorities(new HashSet<>(authentication.getGrantedAuthorities()));
		wapiRequest.setRedirectUri(authentication.getRedirectUri());
		wapiRequest.setRequestParameters(authentication.getRequestParameters());
		if(isNotEmpty(authentication.getResourceIds()))
			wapiRequest.setResourceIds(new HashSet<>(authentication.getResourceIds()));
		if(isNotEmpty(authentication.getResponseTypes()))
			wapiRequest.setResponseTypes(new HashSet<>(authentication.getResponseTypes()));
		if(isNotEmpty(authentication.getScope()))
			wapiRequest.setScope(new HashSet<>(authentication.getScope()));
		
		wapiAuthentication.setStoredRequest(wapiRequest);
		
		logger.info("restored entity {} to authentication {}", authentication, wapiAuthentication);
		
		return wapiAuthentication;
	}

	public void removeAuthentication(MongoOAuth2Authentication authentication) {
		if(authentication != null) {
			logger.info("removing authentication {}", authentication);
			
			authenticationRepository.delete(authentication);
		}
	}
}
