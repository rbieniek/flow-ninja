/**
 * 
 */
package org.flowninja.rspl.client.arin;

import java.util.HashSet;
import java.util.Set;

import org.flowninja.rspl.client.common.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.springframework.stereotype.Component;

/**
 * Resolving network information according to this URI:http://whois.arin.net/rest/ip/a.b.c.d
 * 
 * This is the RESTFul service provided by ARIN
 * 
 * @author rainer
 *
 */
@Component
public class ArinNetworkResourceResolver implements INetworkResourceResolver {

	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();

	public ArinNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)3, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)4, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)6, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)7, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)8, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)9, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)11, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)12, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)13, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)15, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)16, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)17, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)18, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)19, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)20, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)21, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)22, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)23, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)24, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)26, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)28, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)29, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)30, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)32, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)33, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)34, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)35, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)38, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)40, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)44, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)45, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)47, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)48, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)50, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)52, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)54, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)55, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)56, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)63, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)64, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)65, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)66, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)67, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)68, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)69, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)70, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)71, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)72, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)73, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)74, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)75, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)76, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)96, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)97, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)98, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)99, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)100, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)104, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)107, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)108, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)128, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)129, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)130, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)131, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)132, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)134, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)135, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)136, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)137, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)138, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)139, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)140, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)142, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)143, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)144, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)146, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)147, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)148, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)149, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)152, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)155, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)156, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)157, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)158, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)159, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)160, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)161, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)162, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)164, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)165, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)166, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)167, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)168, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)169, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)170, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)172, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)173, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)174, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)184, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)192, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)198, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)199, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)204, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)205, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)206, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)207, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)208, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)209, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)214, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)215, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)216, 0, 0, 0}, 8));
		
	}
	
	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolveNetworkAddress(byte[])
	 */
	@Override
	public NetworkResource resolveNetworkAddress(byte[] networkAddress) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#resolvingRegistry()
	 */
	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.ARIN;
	}

	@Override
	public boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

}
