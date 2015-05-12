/**
 * 
 */
package org.flowninja.rspl.client.arin;

import org.flowninja.rspl.client.common.INetworkResourceResolver;
import org.flowninja.rspl.client.common.ResolveResult;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;

/**
 * Resolving network information according to this URI:http://whois.arin.net/rest/ip/a.b.c.d
 * 
 * This is the RESTFul service provided by ARIN
 * 
 * @author rainer
 *
 */
public class ArinNetworkResourceResolver implements INetworkResourceResolver {

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolveNetworkAddress(byte[])
	 */
	@Override
	public ResolveResult resolveNetworkAddress(byte[] networkAddress) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolvingRegistry()
	 */
	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.ARIN;
	}

}
