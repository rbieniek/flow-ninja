/**
 * 
 */
package org.flowninja.collector.client.auth;

import java.util.Collection;
import java.util.HashSet;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.flowninja.collector.client.components.AuthorizationClientFactory;

/**
 * @author rainer
 *
 */
@Immutable
@SuppressWarnings("deprecation")
public class BearerSchemeFactory implements AuthSchemeProvider, AuthSchemeFactory {

	public static final String SCHEME = "Bearer";
	public static final Collection<String> SCHEME_COLLECTION = new HashSet<String>();
	
	static {
		SCHEME_COLLECTION.add(SCHEME);
	}
	
	private AuthorizationClientFactory clientFactory;
	
	public BearerSchemeFactory(AuthorizationClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.auth.AuthSchemeFactory#newInstance(org.apache.http.params.HttpParams)
	 */
	@Override
	public AuthScheme newInstance(HttpParams params) {
		return new BearerAuthScheme(clientFactory.createAuthorizationClient());
	}

	/* (non-Javadoc)
	 * @see org.apache.http.auth.AuthSchemeProvider#create(org.apache.http.protocol.HttpContext)
	 */
	@Override
	public AuthScheme create(HttpContext context) {
		return new BearerAuthScheme(clientFactory.createAuthorizationClient());
	}

}
