/**
 * 
 */
package org.flowninja.collector.client.components;


/**
 * @author rainer
 *
 */
public interface AuthorizationClientFactory {
	/**
	 * Create an authorization client
	 * 
	 * @return
	 */
	AuthorizationClient createAuthorizationClient();
}
