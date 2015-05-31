/**
 * 
 */
package org.flowninja.rspl.client.json.lacnic;

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
public class LacnicNetworkResourceResolver implements IJsonNetworkResourceResolver {

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
	public ResultDocument resolveNetworkAddress(byte[] networkAddress) throws URISyntaxException, IOException {
		ResultDocument resource = null;
		
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
