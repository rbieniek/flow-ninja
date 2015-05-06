/**
 * 
 */
package org.flowninja.persistence.generic.services;

import java.util.Set;

import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.types.generic.AccountingGroupKey;

/**
 * Persistence interface for managing accouting groups
 * 
 * @author rainer
 *
 */
public interface IAccountingGroupPersistenceService {
	/**
	 * List  accounting groups 
	 * 
	 * @return
	 */
	public Set<AccountingGroupRecord> listAccountingGroups();

	/**
	 * 
	 * @param key
	 * @return
	 */
	public AccountingGroupRecord findByKey(AccountingGroupKey key);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public AccountingGroupRecord findByName(String name);
	
	/**
	 * Create an accouting group
	 * 
	 * @param name
	 * @param comment
	 * @return
	 * @throws RecordAlreadyExistsException
	 */
	public AccountingGroupRecord createAccountingGroup(String name, String comment) throws RecordAlreadyExistsException;
	
	/**
	 * 
	 * @param record
	 * @return
	 * @throws RecordNotFoundException
	 * @throws RecordAlreadyExistsException
	 */
	public AccountingGroupRecord updateAccoutingGroup(AccountingGroupRecord record) throws RecordNotFoundException, RecordAlreadyExistsException;
	
	/**
	 * 
	 * @param key
	 */
	public void deleteAccountingGroup(AccountingGroupKey key) throws RecordNotFoundException;
}
