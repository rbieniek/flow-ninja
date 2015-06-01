/**
 * 
 */
package org.flowninja.persistence.generic.types.impl;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.persistence.generic.types.IOAuth2RefreshToken;

/**
 * @author rainer
 *
 */
public class OAuth2RefreshTokenImpl implements IOAuth2RefreshToken {

	private String value;
	private Date expiration;
	
	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2RefreshToken#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2RefreshToken#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @return the expiration
	 */
	public Date getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
