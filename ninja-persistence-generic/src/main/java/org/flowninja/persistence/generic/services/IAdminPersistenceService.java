/**
 * 
 */
package org.flowninja.persistence.generic.services;

import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;

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
}
