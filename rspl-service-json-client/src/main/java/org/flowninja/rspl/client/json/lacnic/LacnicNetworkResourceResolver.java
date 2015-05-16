/**
 * 
 */
package org.flowninja.rspl.client.json.lacnic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.rspl.definitions.services.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
// @Component
public class LacnicNetworkResourceResolver implements INetworkResourceResolver {

	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();

	public LacnicNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)177, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)179, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)181, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)186, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)187, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)189, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)190, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)191, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)200, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)201, 0, 0, 0}, 8));
	}
	
	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolveNetworkAddress(byte[])
	 */
	@Override
	public NetworkResource resolveNetworkAddress(byte[] networkAddress)
			throws URISyntaxException, IOException {
		NetworkResource resource = null;
		
		return resource;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolvingRegistry()
	 */
	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.LACNIC;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#canResolveAddress(byte[])
	 */
	@Override
	public boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

}
