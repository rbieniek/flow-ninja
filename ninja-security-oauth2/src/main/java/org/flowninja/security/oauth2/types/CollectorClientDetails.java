/**
 * 
 */
package org.flowninja.security.oauth2.types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.flowninja.persistence.generic.types.CollectorRecord;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * Client details for a data feed collector 
 * 
 * @author rainer
 *
 */
public class CollectorClientDetails implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6997441667346625142L;

	private static final Set<String> AUTHORIZATION_GRANT_TYPES = new HashSet<String>();
	private static final Set<GrantedAuthority> GRANTED_AUTHORITIES = new HashSet<GrantedAuthority>();
	
	static {
		AUTHORIZATION_GRANT_TYPES.add("client_credentials");
		
		GRANTED_AUTHORITIES.add(new FlowNinjaClientGrantedAuthority("collector"));
	}
	
	
	private CollectorRecord collector;
	private int accessTokenValiditySeconds;
	
	/**
	 * 
	 */
	public CollectorClientDetails() {
	}

	public CollectorClientDetails(CollectorRecord collector, int accessTokenValiditySeconds) {
		this.collector = collector;
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getClientId()
	 */
	@Override
	public String getClientId() {
		return collector.getClientId();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getResourceIds()
	 */
	@Override
	public Set<String> getResourceIds() {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#isSecretRequired()
	 */
	@Override
	public boolean isSecretRequired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getClientSecret()
	 */
	@Override
	public String getClientSecret() {
		return collector.getClientSecret();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#isScoped()
	 */
	@Override
	public boolean isScoped() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getScope()
	 */
	@Override
	public Set<String> getScope() {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getAuthorizedGrantTypes()
	 */
	@Override
	public Set<String> getAuthorizedGrantTypes() {
		return AUTHORIZATION_GRANT_TYPES;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getRegisteredRedirectUri()
	 */
	@Override
	public Set<String> getRegisteredRedirectUri() {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getAuthorities()
	 */
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return GRANTED_AUTHORITIES;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getAccessTokenValiditySeconds()
	 */
	@Override
	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getRefreshTokenValiditySeconds()
	 */
	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#isAutoApprove(java.lang.String)
	 */
	@Override
	public boolean isAutoApprove(String scope) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.ClientDetails#getAdditionalInformation()
	 */
	@Override
	public Map<String, Object> getAdditionalInformation() {
		return Collections.emptyMap();
	}

}
