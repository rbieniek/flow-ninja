/**
 * 
 */
package org.flowninja.persistence.rspl.generic.services;

import org.flowninja.persistence.rspl.generic.types.NetworkInformation;
import org.flowninja.types.net.CIDR4Address;

/**
 * @author rainer
 *
 */
public interface INetworkResourcePersistService {
	/**
	 * 
	 * @param address
	 * @param networkInfo
	 */
	public void persistNetworkInformation(CIDR4Address address, NetworkInformation networkInfo);
}
