/**
 * 
 */
package org.flowninja.persistence.generic.services;

import java.util.Set;

import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;

/**
 * @author rainer
 *
 */
public interface IAuthorityPersistenceService {
	/**
	 * List all existing authorities
	 * 
	 * @return
	 */
	public Set<AuthorityRecord> listAuthorities();
	
	/**
	 * Find an authority record by key
	 * 
	 * @param key the authority to look up.
	 * @return the authority record or <code>null</code> if the record cannot be found
	 */
	public AuthorityRecord findAuthorityByKey(AuthorityKey key);
	
	/**
	 * Find an authority record by authority value
	 * 
	 * @param authority the authority to look up.
	 * @return the authority record or <code>null</code> if the record cannot be found
	 */
	public AuthorityRecord findAuthorityByAuthority(String authority);

	/**
	 * Create an authority record
	 *  
	 * @param authority the authority value
	 * @return the created authority record
	 * @throws RecordAlreadyExistsException the authority value already exists
	 */
	public AuthorityRecord insertAuhority(String authority) throws RecordAlreadyExistsException;
	
	/**
	 * Delete an authority record
	 * 
	 * @param key the key of the authority to be deleted
	 * @throws RecordNotFoundException the referenced authority record does not exist
	 */
	public void deleteAuthority(AuthorityKey key) throws RecordNotFoundException;
}
