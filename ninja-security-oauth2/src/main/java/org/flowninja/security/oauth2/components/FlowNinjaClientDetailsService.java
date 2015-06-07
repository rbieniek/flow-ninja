/**
 * 
 */
package org.flowninja.security.oauth2.components;

import org.flowninja.persistence.generic.services.ICollectorPersistenceService;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.security.oauth2.types.CollectorClientDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @author rainer
 *
 */
public class FlowNinjaClientDetailsService implements ClientDetailsService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(FlowNinjaClientDetailsService.class);
	
	@Autowired
	private ICollectorPersistenceService collectorPersistence;
	
	@Autowired
	private Environment environment;
	
	private int accessTokenValiditySeconds = 30;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetailsService#loadClientByClientId(java.lang.String)
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientDetails details = null;
		CollectorRecord collector = null;
		
		logger.info("loading client by client ID {}", clientId);
		
		if((collector = collectorPersistence.findCollectoryByClientID(clientId)) != null) {
			details = new CollectorClientDetails(collector, accessTokenValiditySeconds);
		}
		
		if(details == null) {
			logger.info("cannot load client by client ID {}", clientId);
			
			throw new ClientRegistrationException("unknown client ID: " + clientId);
		}
		
		return details;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		accessTokenValiditySeconds = environment.getProperty("flowninja.oauth.lifetime.access", Integer.class, 3600);
		
		logger.info("setting access token lifetime to {} seconds", accessTokenValiditySeconds);
	}

}
