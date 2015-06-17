/**
 * 
 */
package org.flowninja.collector.client.components.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class RestClientDetails {

	@JsonProperty
	private String token;
	
	@JsonProperty
	private String type;
	
	public RestClientDetails(String token, String type) {
		this.token = token;
		this.type = type;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	
}
