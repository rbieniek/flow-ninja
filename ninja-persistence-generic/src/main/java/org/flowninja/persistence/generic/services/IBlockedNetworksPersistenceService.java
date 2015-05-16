/**
 * 
 */
package org.flowninja.persistence.generic.services;

import java.util.Set;

import org.flowninja.persistence.generic.types.BlockedNetworkRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.flowninja.types.net.CIDR4Address;

/**
 * Persistence service fo maintaining a set of networks which are administratively blocked from WHOIS lookup
 * 
 * @author rainer
 *
 */
public interface IBlockedNetworksPersistenceService {
	/**
	 * List all blocked networks
	 * 
	 * @return
	 */
	public Set<BlockedNetworkRecord> listBlockedNetworks();

	/**
	 * find a blocked network record by the address
	 * @param address
	 * @return
	 */
	public BlockedNetworkRecord findBlockedNetwork(CIDR4Address address);

	/**
	 * find a blocked network for a given host (/32) address
	 * 
	 * @param address a CIDR host address
	 * @return
	 */
	public BlockedNetworkRecord findContainingBlockedNetwork(CIDR4Address address);

	/**
	 * find a blocked network record by the key
	 * 
	 * @param address
	 * @return
	 */
	public BlockedNetworkRecord findBlockedNetwork(BlockedNetworkKey key);

	/**
	 * add a blocked network
	 * 
	 * @param address
	 * @return
	 * @throws RecordAlreadyExistsException
	 */
	public BlockedNetworkRecord addBlockedNetwork(CIDR4Address address) throws RecordAlreadyExistsException;
	
	/**
	 * removes a blocked network from the persistence store
	 * 
	 * @param key
	 * @throws RecordNotFoundException
	 */
	public void deleteBlockedNetwork(BlockedNetworkKey key) throws RecordNotFoundException;
}
