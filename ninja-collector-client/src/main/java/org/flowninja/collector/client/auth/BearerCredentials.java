/**
 * 
 */
package org.flowninja.collector.client.auth;

import java.io.Serializable;
import java.net.URI;
import java.security.Principal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BearerCredentials))
			return false;
		
		BearerCredentials o = (BearerCredentials)obj;
		
		return (new EqualsBuilder())
				.append(this.authorizationEndpoint, o.authorizationEndpoint)
				.append(this.principal, o.principal)
				.append(this.secret, o.secret)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(authorizationEndpoint)
				.append(principal)
				.append(secret)
				.toHashCode();
	}
}
