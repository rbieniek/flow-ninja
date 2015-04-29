/**
 * 
 */
package org.flowninja.persistence.generic.services;

import java.util.Set;

import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;

/**
 * Service interface to be implemented by persistence providers for admin records
 * 
 * @author rainer
 *
 */
public interface IAdminPersistenceService {
	/**
	 * Login an admin. 
	 * 
	 * @param userName user name
	 * @param password password
	 * @return the admin record if login is sucessful, <code>null</code> otherwise
	 */
	public AdminRecord login(String userName, String password);

	/**
	 * Find an admin record by the user name
	 * 
	 * @param userName the user name
	 * @return the admin record or <code>null</code>
	 */
	public AdminRecord findByUserName(String userName);

	/**
	 * Find an admin record by the key
	 *  
	 * @param key the admin key to look up
	 * @return the admin record or <code>null</code>
	 */
	public AdminRecord findByKey(AdminKey key);
	
	/**
	 * List all admin records
	 * 
	 * @return a set of all known admin records
	 */
	public Set<AdminRecord> listAdmins();
	
	/**
	 * Create an admin record
	 * 
	 * @param userName the user name
	 * @param password the password
	 * @param authorities authorites assigned to admin
	 * 
	 * @return
	 * 
	 * @throws RecordAlreadyExistsException admin account already exists
	 */
	public AdminRecord createAdmin(String userName, String password,
			Set<AuthorityKey> authorities) throws RecordAlreadyExistsException;
	
	/**
	 * Assign authorities to an admin
	 * 
	 * @param key
	 * @param authorities
	 * @return
	 * @throws RecordNotFoundException admin not found 
	 */
	public AdminRecord assignAuthorities(AdminKey key, Set<AuthorityKey> authorities) throws RecordNotFoundException;
	
	/**
	 * delete an admin record
	 * 
	 * @param key
	 * @throws RecordNotFoundException
	 */
	public void deleteAdmin(AdminKey key) throws RecordNotFoundException;
}
