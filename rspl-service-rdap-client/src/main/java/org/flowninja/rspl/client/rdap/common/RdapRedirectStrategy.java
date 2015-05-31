/**
 * 
 */
package org.flowninja.rspl.client.rdap.common;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class RdapRedirectStrategy extends DefaultRedirectStrategy {

	/**
	 * RDAP.org is supposed to issue redirects only on GET requests as per spec.
	 */
	@Override
	protected boolean isRedirectable(String method) {
		return StringUtils.equalsIgnoreCase(method, HttpGet.METHOD_NAME);
	}

    @Override
    public boolean isRedirected(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
    	boolean doRedirect = false;
    	
        Args.notNull(request, "HTTP request");
        Args.notNull(response, "HTTP response");

        final int statusCode = response.getStatusLine().getStatusCode();
        final String method = request.getRequestLine().getMethod();
        final Header locationHeader = response.getFirstHeader("location");
        
        switch (statusCode) {
        case HttpStatus.SC_MOVED_PERMANENTLY:
            if(isRedirectable(method) && locationHeader != null) {
            	URI uri = createLocationURI(locationHeader.getValue());
            	
            	// we cannot allow forwards to ARIN because ARIN is not responding with RDAP objects
            	// we can allow forawrds to APNIC and LACNIC
            	doRedirect = !StringUtils.containsIgnoreCase(uri.getHost(), "arin.net") 
            			&& (StringUtils.containsIgnoreCase(uri.getHost(), "lacnic.net") 
            					|| StringUtils.containsIgnoreCase(uri.getHost(), "apnic.net"));
            }
        }
        
        return doRedirect;
    }
}
