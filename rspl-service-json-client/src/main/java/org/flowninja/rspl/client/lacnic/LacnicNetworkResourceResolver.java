/**
 * 
 */
package org.flowninja.rspl.client.lacnic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.rspl.client.common.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class LacnicNetworkResourceResolver implements INetworkResourceResolver {

	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();

	public LacnicNetworkResourceResolver() {
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
	public NetworkResource resolveNetworkAddress(byte[] networkAddress)
			throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
		return null;
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
