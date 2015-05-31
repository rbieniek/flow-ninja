/**
 * 
 */
package org.flowninja.persistence.generic.types.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.persistence.generic.types.WapiOAuth2AccessToken;
import org.flowninja.persistence.generic.types.WapiOAuth2Authentication;
import org.flowninja.persistence.generic.types.WapiOAuth2RefreshToken;

/**
 * @author rainer
 *
 */
public class DefaultWapiOAuth2AccessToken implements WapiOAuth2AccessToken {

	private String value;
	private Map<String, Object> infos;
	private Date expiration;
	private Set<String> scope;
	private WapiOAuth2RefreshToken refreshToken;
	private WapiOAuth2Authentication authentication;
 	
	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#setAdditionalInformation(java.util.Map)
	 */
	@Override
	public void setAdditionalInformation(Map<String, Object> infos) {
		this.infos = infos;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#getAdditionalInformation()
	 */
	@Override
	public Map<String, Object> getAdditionalInformation() {
		return infos;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#setExpiration(java.util.Date)
	 */
	@Override
	public void setExpiration(Date date) {
		this.expiration = date;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#getExpiration()
	 */
	@Override
	public Date getExpiration() {
		return expiration;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#setScope(java.util.Set)
	 */
	@Override
	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2AccessToken#getScope()
	 */
	@Override
	public Set<String> getScope() {
		return scope;
	}

	@Override
	public void setRefreshToken(WapiOAuth2RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public WapiOAuth2RefreshToken getRefreshToken() {
		return refreshToken;
	}

	@Override
	public void setAuthentication(WapiOAuth2Authentication authentication) {
		this.authentication = authentication;
	}

	@Override
	public WapiOAuth2Authentication getAuthentication() {
		return authentication;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
