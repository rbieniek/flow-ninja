/**
 * 
 */
package org.flowninja.rspl.client.apnic;

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
public class ApnicNetworkResourceResolver implements INetworkResourceResolver {

	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();

	public ApnicNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)1, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)14, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)27, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)36, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)39, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)42, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)43, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)49, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)58, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)59, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)60, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)61, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)101, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)103, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)106, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)110, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)111, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)112, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)113, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)114, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)115, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)116, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)117, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)118, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)119, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)120, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)121, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)122, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)123, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)124, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)125, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)126, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)133, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)150, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)153, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)163, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)171, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)175, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)180, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)182, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)183, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)202, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)203, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)210, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)211, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)218, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)219, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)220, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)221, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)222, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)223, 0, 0, 0}, 8));		
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
		return ENetworkRegistry.APNIC;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#canResolveAddress(byte[])
	 */
	@Override
	public boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

}
