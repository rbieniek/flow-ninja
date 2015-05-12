/**
 * 
 */
package org.flowninja.rspl.definitions;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses an address range in the format 'aaa.bbb.ccc.ddd - eee.fff.ggg.hhh' into a {@link CIDR4Address} instance
 * 
 * @author rainer
 * @see CIDR4Address
 *
 */
public class CIDR4AddressRangeParser {
	private static final Logger logger = LoggerFactory.getLogger(CIDR4AddressRangeParser.class);
	
	public static CIDR4Address parse(String rangeSpec) {
		logger.info("parsing range specification '{}'", rangeSpec);
		
		String[] parts = StringUtils.split(rangeSpec, "-");
		
		if(parts.length != 2) {
			logger.warn("invalid address specification: range specification '{}' has invalid numer of parts: {}", rangeSpec, parts.length);
			
			throw new IllegalArgumentException("invalid address range specification passed");
		}
		
		byte[] rangeFrom = NetworkAddressHelper.parseAddressSpecification(parts[0].trim());
		byte[] rangeTo = NetworkAddressHelper.parseAddressSpecification(parts[1].trim());
		
		byte[] masked = new byte[rangeFrom.length];
		
		for(int i=0; i<rangeFrom.length; i++)
			masked[i] = (byte)((rangeFrom[i] ^ rangeTo[i]) & 0xff);
		
		int mask = 0;
		boolean haveDiff = false;
		
		for(int octet=0; octet<masked.length && !haveDiff; octet++) {
			for(int bit = 7; bit >= 0; bit--) {
				if((masked[octet] & (1 << bit)) > 0) {
					haveDiff = true;
					
					break;
				}
				
				mask++;
			}
		}
		
		return new CIDR4Address(rangeFrom, mask);
	}
}
