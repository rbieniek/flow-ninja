/**
 * 
 */
package org.flowninja.collector.client.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.auth.RFC2617Scheme;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.flowninja.collector.client.components.AuthorizationClient;
import org.flowninja.collector.client.components.BearerToken;

/**
 * OAuth2 bearer token-based auth scheme which handles 
 * <ul>
 * <li>the OAuth2 bearer token usage defined in RFC 6750</li>
 * <li>the OAuth2 client credentials flow defined in RFC 6749 sec. 4.4</li>
 * </ul>
 * 
 * @author rainer
 *
 * @see https://tools.ietf.org/html/rfc6749
 * @see https://tools.ietf.org/html/rfc6750
 */
public class BearerAuthScheme extends RFC2617Scheme {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4440576029558367424L;

	private transient AuthorizationClient authorizationClient;
	
	public BearerAuthScheme(AuthorizationClient authorizationClient) {
		this.authorizationClient = authorizationClient;
	}

	@Override
	public String getSchemeName() {
		return "bearer";
	}

	@Override
	public boolean isConnectionBased() {
		return false;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
		if(!(credentials instanceof BearerCredentials))
			throw new AuthenticationException("Incompatible credentials of type " + credentials.getClass().getName());
		
		BearerToken token = authorizationClient.grantClientCredentials((BearerCredentials)credentials, getParameter("scope"));
		
        final CharArrayBuffer buffer = new CharArrayBuffer(32);
        
        buffer.append(AUTH.WWW_AUTH_RESP);
        buffer.append(": Bearer ");
        buffer.append(token.getToken());
        
		return new BufferedHeader(buffer);
	}

}
