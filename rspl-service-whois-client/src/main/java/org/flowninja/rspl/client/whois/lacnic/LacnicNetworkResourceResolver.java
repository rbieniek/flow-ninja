/**
 * 
 */
package org.flowninja.rspl.client.whois.lacnic;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.whois.common.InetnumRecord;
import org.flowninja.rspl.client.whois.common.WhoisClientBase;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class LacnicNetworkResourceResolver extends WhoisClientBase {


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

	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.LACNIC;
	}

	@Override
	protected String whoisHost() {
		return "whois.lacnic.net";
	}

	@Override
	protected NetworkResource mapInetNumRecord(InetnumRecord record) {
		NetworkResource resource = null;
		
		if(StringUtils.isNotBlank(record.getInetnum()) && StringUtils.isNotBlank(record.getOwner())) {
			int idx = StringUtils.indexOf(record.getInetnum(), "/");
			
			if(idx > 0) {
				StringBuffer addrSpec = new StringBuffer(StringUtils.substring(record.getInetnum(), 0, idx));
				
				int dots = StringUtils.countMatches(addrSpec, ".");

				while(dots < 3) {
					addrSpec.append(".0");
					dots++;
				}
				
				resource = new NetworkResource(new CIDR4Address(NetworkAddressHelper.parseAddressSpecification(addrSpec.toString()),
							Integer.parseInt(StringUtils.substring(record.getInetnum(), idx+1))), 
						record.getOwner(), null, ENetworkRegistry.LACNIC);
			}
		}

		return resource;
	}
}
