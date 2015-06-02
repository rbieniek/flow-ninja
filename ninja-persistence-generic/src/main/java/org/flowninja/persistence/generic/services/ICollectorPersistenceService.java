/**
 * 
 */
package org.flowninja.persistence.generic.services;

import java.util.Set;

import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;

/**
 * Interface to be implemented by a persistence service implementation responsilbe for
 * data feed collector information
 * 
 * @author rainer
 *
 */
public interface ICollectorPersistenceService {
	/**
	 * List all collectors for a given accouting group key
	 * 
	 * @param groupKey the accouting group key
	 * @return a set of collectors beloning to an accounting group. May by empty, is never <code>null</code>
	 */
	public Set<CollectorRecord> listCollectorsForAccountingGroup(AccountingGroupKey groupKey);

	/**
	 * Delete all collectors for a given accouting group key
	 * 
	 * @param groupKey the accouting group key
	 */
	public void deleteCollectorsForAccountingGroup(AccountingGroupKey groupKey);

	/**
	 * Find a collector by the key
	 * 
	 * @param key the key used for addressing the collector
	 * @return the collector record or <code>null</code> if not found
	 */
	public CollectorRecord findCollectoryByKey(CollectorKey key);
	
	/**
	 * Find a collector by the name
	 * 
	 * @param name the name of the collector
	 * @return the collector record or <code>null</code> if not found
	 */
	public CollectorRecord findCollectoryByName(String name);
	
	/**
	 * Find a collector by the client ID
	 * 
	 * @param clientId the client ID (used in OAuth2 authentication) of the collector
	 * @return the collector record or <code>null</code> if not found
	 */
	public CollectorRecord findCollectoryByClientID(String clientID);
	
	/**
	 * Create a collector record
	 * 
	 * @param groupKey the accounting group owning thecollector 
	 * @param name the name of the collector
	 * @param comment an explanatory comment 
	 * @return the created and initialised collector record
	 * @throws RecordAlreadyExistsException the collector name already exists in the persistence store
	 * @throws RecordNotFoundException the accouting group owning the collector does not exists
	 */
	public CollectorRecord createCollector(AccountingGroupKey groupKey, String name, String comment) throws RecordAlreadyExistsException, RecordNotFoundException;
	
	/**
	 * Update a collector data record. the following items can be updated:
	 * 
	 * <ul>
	 * <li>Name</li>
	 * <li>Comment</li>
	 * </ul>
	 * 
	 * @param update the update to be applied
	 * @return the updated data record
	 * @throws RecordNotFoundException the addressed collector record does not exists
	 * @throws RecordAlreadyExistsException the assigned name already exists
	 */
	public CollectorRecord updateCollector(CollectorRecord update) throws RecordAlreadyExistsException, RecordNotFoundException;

	/**
	 * Assign a new client ID / client secret key pair to the collector
	 * 
	 * @param key the collector key
	 * @return the collector record with the updated client ID / client secret pair
	 * @throws RecordNotFoundException the addressed collector record does not exists
	 */
	public CollectorRecord recreateCredentials(CollectorKey key) throws RecordNotFoundException;
	
	/**
	 * Delete a collector record
	 * 
	 * @param key the collector key
	 * @throws RecordNotFoundException the addressed collector record does not exists
	 */
	public void deleteCollector(CollectorKey key) throws RecordNotFoundException;
}
