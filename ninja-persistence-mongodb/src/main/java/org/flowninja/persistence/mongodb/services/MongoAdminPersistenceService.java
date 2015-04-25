/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class MongoAdminPersistenceService implements IAdminPersistenceService {

	@Autowired
	private IMongoAdminRepository repository;
	
	@Override
	public AdminRecord login(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminRecord findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminRecord findByKey(AdminKey key) {
		// TODO Auto-generated method stub
		return null;
	}

}
