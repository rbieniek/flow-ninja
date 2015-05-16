/**
 * 
 */
package org.flowninja.rspl.client.whois.afrinic;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.whois.common.InetnumRecord;
import org.flowninja.rspl.client.whois.common.WhoisClientBase;
import org.flowninja.rspl.definitions.CIDR4AddressRangeParser;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class AfrinicNetworkResourceResolver extends WhoisClientBase {


	public AfrinicNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)41, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)102, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)105, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)154, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)196, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)197, 0, 0, 0}, 8));
	}

	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.AFRINIC;
	}

	@Override
	protected String whoisHost() {
		return "whois.afrinic.net";
	}

	@Override
	protected NetworkResource mapInetNumRecord(InetnumRecord record) {
		NetworkResource resource = null;
		
		if(StringUtils.isNotBlank(record.getInetnum()) && StringUtils.isNotBlank(record.getNetname()) &&StringUtils.startsWith(record.getSource(), "AFRINIC"))
			resource = new NetworkResource(CIDR4AddressRangeParser.parse(record.getInetnum()), record.getNetname(), 
												record.getCountry(), ENetworkRegistry.AFRINIC);

		return resource;
	}
}
