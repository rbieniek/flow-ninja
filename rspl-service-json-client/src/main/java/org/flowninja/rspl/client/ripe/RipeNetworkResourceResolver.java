/**
 * 
 */
package org.flowninja.rspl.client.ripe;

import org.apache.http.impl.client.CloseableHttpClient;
import org.flowninja.rspl.client.common.INetworkResourceResolver;
import org.flowninja.rspl.client.common.ResolveResult;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Resolving network information according to this URI: http://rest.db.ripe.net/search.json?query-string=a.b.c.d&type-filter=inetnum&flags=resource
 * 
 * This is the RESTFul service provided by ARIN
 * 
 * @author rainer
 *
 */
public class RipeNetworkResourceResolver implements INetworkResourceResolver {

	@Autowired
	private CloseableHttpClient httpClient;
	
	@Override
	public ResolveResult resolveNetworkAddress(byte[] networkAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.RIPE;
	}

}
