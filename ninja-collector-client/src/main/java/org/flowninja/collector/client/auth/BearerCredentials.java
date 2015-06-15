/**
 * 
 */
package org.flowninja.collector.client.auth;

import java.io.Serializable;
import java.net.URI;
import java.security.Principal;

import org.apache.http.auth.Credentials;

/**
 * @author rainer
 *
 */
public class BearerCredentials implements Credentials, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8606896960302046214L;

	private BearerKeyPrincipal principal;
	private String secret;
	private URI authorizationEndpoint;
	
	public BearerCredentials() {
	}

	public BearerCredentials(String key, String secret, URI authorizationEndpoint) {
		this.principal = new BearerKeyPrincipal(key);
		this.secret = secret;
		this.authorizationEndpoint = authorizationEndpoint;
	}


	/* (non-Javadoc)
	 * @see org.apache.http.auth.Credentials#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	/* (non-Javadoc)
	 * @see org.apache.http.auth.Credentials#getPassword()
	 */
	@Override
	public String getPassword() {
		return secret;
	}

	/**
	 * @return the authorizationEndpoint
	 */
	public URI getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	
}
