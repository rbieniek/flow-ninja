/**
 * 
 */
package org.flowninja.persistence.generic.services;

import org.flowninja.persistence.generic.types.IAccessTokenKey;
import org.flowninja.persistence.generic.types.IOAuth2AccessToken;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;

/**
 * @author rainer
 *
 */
public interface IOAuth2TokenPersistenceService {

	/**
	 * Store the access token. The token must contain authentication information and may contain a refresh token
	 * @param wapiToken
	 */
	<V extends IAccessTokenKey<?>> V storeAccessToken(IOAuth2AccessToken wapiToken);

	/**
	 * 
	 * @param value
	 * @return
	 */
	IOAuth2AccessToken readAccessTokenForValue(String value);

	/**
	 * 
	 * @param value
	 * @return
	 */
	IOAuth2Authentication readAuthenticationForAccessToken(String value);

	/**
	 * 
	 * @param value
	 */
	void removeAccessToken(String value);
}
