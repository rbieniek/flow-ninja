/**
 * 
 */
package org.flowninja.security.oauth2.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author rainer
 *
 */
public class FlowNinjaTokenLifetimeEnhancer implements TokenEnhancer, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(FlowNinjaTokenLifetimeEnhancer.class);
	
	@Autowired
	private Environment environment;
	
	private int accessTokenLifetime = 3600;
	private int refreshTokenLifetime = 86400;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenEnhancer#enhance(org.springframework.security.oauth2.common.OAuth2AccessToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		return accessToken;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		accessTokenLifetime = environment.getProperty("flowninja.oauth.lifetime.access", Integer.class, 3600);
		refreshTokenLifetime = environment.getProperty("flowninja.oauth.lifetime.refresh", Integer.class, 86400);
	}

}
