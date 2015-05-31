/**
 * 
 */
package org.flowninja.persistence.generic.services;

import org.flowninja.persistence.generic.types.WapiAccessTokenKey;
import org.flowninja.persistence.generic.types.WapiOAuth2AccessToken;
import org.flowninja.persistence.generic.types.WapiOAuth2Authentication;

/**
 * @author rainer
 *
 */
public interface WapiOAuth2TokenStore {

	/**
	 * Store the access token. The token must contain authentication information and may contain a refresh token
	 * @param wapiToken
	 */
	<V extends WapiAccessTokenKey<?>> V storeAccessToken(WapiOAuth2AccessToken wapiToken);

	/**
	 * 
	 * @param value
	 * @return
	 */
	WapiOAuth2AccessToken readAccessTokenForValue(String value);

	/**
	 * 
	 * @param value
	 * @return
	 */
	WapiOAuth2Authentication readAuthenticationForAccessToken(String value);

	/**
	 * 
	 * @param value
	 */
	void removeAccessToken(String value);
}
