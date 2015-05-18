/**
 * 
 */
package org.flowninja.persistence.rspl.generic.services;

import org.flowninja.persistence.rspl.generic.types.NetworkInformation;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class RsplPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(RsplPersistenceService.class);
	
	private INetworkResourcePersistService persistService;
	private INetworkResourceLoadService loadService;
	
	/**
	 * Save a network resource
	 * 
	 * @param resource
	 */
	public void persistNetworkResource(NetworkResource resource) {
		logger.info("writing network resource {}", resource);
		
		try {
			persistService.writeNetworkInformation(resource.getNetworkAddress(), 
					new NetworkInformation(resource.getName(), 
							resource.getCountry(), 
							resource.getRegionalInternetRegistry()));
		} catch(Exception e) {
			logger.error("failed to persist network resource", e);
		}
	}
	
	public NetworkResource loadNetworkResource(CIDR4Address address) {
		NetworkResource resource = null;
		
		logger.info("loading network resource for address {}", address);
		
		if(address.isHostAddress()) {
			for(int maskBits = 30; maskBits >= 8; maskBits--) {
				NetworkInformation networkInfo ;
				CIDR4Address netAddress = new CIDR4Address(address.getAddress(), maskBits);
				
				if((networkInfo = loadService.readNetworkInformation(netAddress)) != null) {
					resource = new NetworkResource(netAddress, networkInfo.getName(), networkInfo.getCountry(), networkInfo.getRegionalInternetRegistry());
					
					break;
				}
			}
		}

		logger.info("loading network resource for address {} yielded {}", address, resource);

		return resource;
	}
}
