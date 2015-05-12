/**
 * 
 */
package org.flowninja.rspl.client.common;

import org.flowninja.rspl.definitions.types.ENetworkRegistry;

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
	public ResolveResult resolveNetworkAddress(byte[] networkAddress);
	
	/**
	 * 
	 * @return
	 */
	public ENetworkRegistry resolvingRegistry();
}
