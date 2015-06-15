/**
 * 
 */
package org.flowninja.collector.client.components;

import org.apache.http.auth.AuthenticationException;
import org.flowninja.collector.client.auth.BearerCredentials;

/**
 * @author rainer
 *
 */
public interface AuthorizationClient {

	/**
	 * Execute the client credentials flow 
	 */
	public BearerToken grantClientCredentials(BearerCredentials creds, String scope) throws AuthenticationException;
}
