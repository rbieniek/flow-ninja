/**
 * 
 */
package org.flowninja.persistence.generic.types.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.IOAuth2Request;

/**
 * @author rainer
 *
 */
public class OAuth2AuthenticationImpl implements IOAuth2Authentication {

	private IOAuth2Request storedRequest;
	private byte[] authentication;
	
	@Override
	public void setStoredRequest(IOAuth2Request storedRequest) {
		this.storedRequest = storedRequest;
	}

	@Override
	public IOAuth2Request getStoredRequest() {
		return storedRequest;
	}

	@Override
	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	@Override
	public byte[] getAuthentication() {
		return authentication;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
