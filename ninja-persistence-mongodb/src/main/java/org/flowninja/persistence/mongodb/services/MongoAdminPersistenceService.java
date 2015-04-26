/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAdminRepository;
import org.flowninja.persistence.mongodb.repositories.IMongoAuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class MongoAdminPersistenceService implements IAdminPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoAdminPersistenceService.class);

	@Autowired
	private IMongoAdminRepository adminRepository;

	@Autowired
	private IMongoAuthorityRepository authorityRepository;

	@Override
	public AdminRecord login(String userName, String password) {
		return null;
	}

	@Override
	public AdminRecord findByUserName(String userName) {
		return null;
	}

	@Override
	public AdminRecord findByKey(AdminKey key) {
		return null;
	}

}
