/**
 * 
 */
package org.flowninja.rspl.definitions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class NetworkAddressHelper {
	private static final Logger logger = LoggerFactory.getLogger(NetworkAddressHelper.class);

	/**
	 * parse an address in the format 'aaa.bbb.ccc.ddd' into a byte array with the length four
	 * 
	 * @param addrSpec
	 * @return
	 */
	public static byte[] parseAddressSpecification(String addrSpec) {
		String[] parts = StringUtils.split(addrSpec, ".");
		if(parts.length != 4) {
			logger.warn("invalid address specification: addres specification '{}' has invalid numer of parts: {}", addrSpec, parts.length);
			
			throw new IllegalArgumentException("invalid address specification passed: " + addrSpec);
		}
		
		byte[] addr = new byte[4];
		
		for(int i=0; i < parts.length; i++) {
			try {
				int v = Integer.parseInt(parts[i]);
	
				if(v < 0 || v > 255) {
					logger.warn("invalid address specification: part value '{}' out of range", parts[i]);
	
					throw new IllegalArgumentException("invalid address specification passed");		
				}
				
				addr[i] = (byte)v;
			} catch(NumberFormatException e) {
				logger.warn("invalid address specification: failed to parse part value '{}'", parts[i], e);
	
				throw new IllegalArgumentException("invalid address specification passed", e);
			}
		}
	
		return addr;
	}
	
	public static String formatAddressSpecification(byte[] address) {
		return String.format("%d.%d.%d.%d", address[0] & 0xff, address[1] & 0xff, address[2] & 0xff, address[3] & 0xff);
	}

}
