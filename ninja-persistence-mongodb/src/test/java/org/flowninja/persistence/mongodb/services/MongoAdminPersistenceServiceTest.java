/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.mongodb.data.MongoAdminRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAdminRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfig.class})
public class MongoAdminPersistenceServiceTest {
	@Autowired
	private MongoAdminPersistenceService persistence;
	
	@Autowired
	private IMongoAdminRepository repository;
	
	private AdminKey key;
	private MongoAdminRecord record;
	
	@Before
	public void prepareDatabase() {
		repository.findAll().forEach((n) -> repository.delete(n));
		
		key = new AdminKey();
		
	}
	
	@Test
	public void loginExistingAdmin() {}
}
