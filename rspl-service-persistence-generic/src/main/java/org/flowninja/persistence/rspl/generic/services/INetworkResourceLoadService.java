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
public interface INetworkResourceLoadService {

	/**
	 * 
	 * @param address
	 * @return
	 */
	public NetworkInformation loadNetworkInformation(CIDR4Address address);
}
