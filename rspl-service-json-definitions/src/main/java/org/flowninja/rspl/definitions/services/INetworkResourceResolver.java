/**
 * 
 */
package org.flowninja.rspl.definitions.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.ResultDocument;

/**
 * @author rainer
 *
 */
public interface INetworkResourceResolver {
	/**
	 * 
	 * @param networkAddress
	 * @return
	 */
	public ResultDocument resolveNetworkAddress(byte[] networkAddress) throws URISyntaxException, IOException;
	
	/**
	 * 
	 * @return
	 */
	public ENetworkRegistry resolvingRegistry();
	
	/**
	 * Check if the service could resolve this address
	 * 
	 * @param networkAddress
	 * @return
	 */
	public boolean canResolveAddress(byte[] networkAddress);
}
