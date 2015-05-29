/**
 * 
 */
package org.flowninja.rspl.client.json.afrinic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.rspl.client.json.common.IJsonNetworkResourceResolver;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.ResultDocument;
import org.flowninja.types.net.CIDR4Address;

/**
 * @author rainer
 *
 */
// @Component
public class AfrinicNetworkResourceResolver implements IJsonNetworkResourceResolver {

	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();

	public AfrinicNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)41, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)102, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)105, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)154, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)196, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)197, 0, 0, 0}, 8));
	}
	
	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolveNetworkAddress(byte[])
	 */
	@Override
	public ResultDocument resolveNetworkAddress(byte[] networkAddress)
			throws URISyntaxException, IOException {
		ResultDocument resource = null;
		
		return resource;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolvingRegistry()
	 */
	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.AFRINIC;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#canResolveAddress(byte[])
	 */
	@Override
	public boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

}
