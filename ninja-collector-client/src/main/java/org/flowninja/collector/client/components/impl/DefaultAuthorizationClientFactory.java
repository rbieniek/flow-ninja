/**
 * 
 */
package org.flowninja.collector.client.components.impl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.flowninja.collector.client.components.AuthorizationClient;
import org.flowninja.collector.client.components.AuthorizationClientFactory;

/**
 * @author rainer
 *
 */
public class DefaultAuthorizationClientFactory implements AuthorizationClientFactory {

	
	
	/* (non-Javadoc)
	 * @see org.flowninja.collector.client.components.AuthorizationClientFactory#createAuthorizationClient()
	 */
	@Override
	public AuthorizationClient createAuthorizationClient() {
		AuthorizationClient client = null;
		
		return client;
	}

}
