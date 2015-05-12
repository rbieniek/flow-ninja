/**
 * 
 */
package org.flowninja.rspl.client.common;

import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;

/**
 * @author rainer
 *
 */
public class ResolveResult {

	private NetworkResource resource;
	private ENetworkRegistry referredRegistry;
	
	/**
	 * 
	 */
	public ResolveResult(NetworkResource resource) {
		this.resource = resource;
	}

	/**
	 * 
	 */
	public ResolveResult(ENetworkRegistry referredRegistry) {
		this.referredRegistry = referredRegistry;
	}

	/**
	 * @return the resource
	 */
	public NetworkResource getResource() {
		return resource;
	}

	/**
	 * @return the referredRegistry
	 */
	public ENetworkRegistry getReferredRegistry() {
		return referredRegistry;
	}

}
