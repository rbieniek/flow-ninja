/**
 * 
 */
package org.flowninja.persistence.generic.types.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.persistence.generic.types.WapiOAuth2Authentication;
import org.flowninja.persistence.generic.types.WapiOAuth2Request;

/**
 * @author rainer
 *
 */
public class DefaultWapiOAuth2Authentication implements WapiOAuth2Authentication {

	private WapiOAuth2Request storedRequest;
	private byte[] authentication;
	
	@Override
	public void setStoredRequest(WapiOAuth2Request storedRequest) {
		this.storedRequest = storedRequest;
	}

	@Override
	public WapiOAuth2Request getStoredRequest() {
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
