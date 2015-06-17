/**
 * 
 */
package org.flowninja.collector.client.components.impl;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.flowninja.collector.client.components.AuthorizationClient;
import org.flowninja.collector.client.components.AuthorizationClientFactory;

/**
 * @author rainer
 *
 */
public class DefaultAuthorizationClientFactory implements AuthorizationClientFactory {

	private HttpClientConnectionManager connectionManager;
	
	public DefaultAuthorizationClientFactory() {
		this(new PoolingHttpClientConnectionManager());
	}

	public DefaultAuthorizationClientFactory(HttpClientConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	/* (non-Javadoc)
	 * @see org.flowninja.collector.client.components.AuthorizationClientFactory#createAuthorizationClient()
	 */
	@Override
	public AuthorizationClient createAuthorizationClient() {
		return new AuthorizationClientImpl(HttpClients.custom()
						.setConnectionManager(this.connectionManager)
						.build());
	}

}
