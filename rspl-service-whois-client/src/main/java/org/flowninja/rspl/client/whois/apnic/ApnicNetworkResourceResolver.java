/**
 * 
 */
package org.flowninja.rspl.client.whois.apnic;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.whois.common.InetnumRecord;
import org.flowninja.rspl.client.whois.common.WhoisClientBase;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class ApnicNetworkResourceResolver extends WhoisClientBase {


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

	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.APNIC;
	}

	@Override
	protected String whoisHost() {
		return "whois.apnic.net";
	}

	@Override
	protected NetworkResource mapInetNumRecord(InetnumRecord record) {
		NetworkResource resource = null;
		
		if(StringUtils.isNotBlank(record.getInetnum()) && StringUtils.isNotBlank(record.getNetname()) &&StringUtils.startsWith(record.getSource(), "APNIC"))
			resource = new NetworkResource(CIDR4AddressRangeParser.parse(record.getInetnum()), record.getNetname(), 
												record.getCountry(), ENetworkRegistry.APNIC);

		return resource;
	}
}
