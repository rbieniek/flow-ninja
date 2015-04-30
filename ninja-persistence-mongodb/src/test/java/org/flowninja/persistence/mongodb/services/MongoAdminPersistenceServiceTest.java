/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.flowninja.persistence.generic.PasswordHasher;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.mongodb.data.MongoAdminRecord;
import org.flowninja.persistence.mongodb.data.MongoAuthorityRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAdminRepository;
import org.flowninja.persistence.mongodb.repositories.IMongoAuthorityRepository;
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
	private IMongoAdminRepository adminRepository;
	
	@Autowired
	private IMongoAuthorityRepository authorityRepository;
	
	@Autowired
	private MongoAdminPersistenceService service;
	
	private MongoAdminRecord adminRecord;
	private MongoAdminRecord userRecord;
	private MongoAuthorityRecord authAdminRec;
	private MongoAuthorityRecord authUserRec;
	
	
	@Before
	public void prepareDatabase() {
		adminRepository.findAll().forEach((n) -> adminRepository.delete(n));
		authorityRepository.findAll().forEach((n) -> authorityRepository.delete(n));

		authAdminRec = new MongoAuthorityRecord("admin", new AuthorityKey());
		authorityRepository.save(authAdminRec);
		authUserRec = new MongoAuthorityRecord("user", new AuthorityKey());
		authorityRepository.save(authUserRec);

		adminRecord = new MongoAdminRecord(new AdminKey(), "admin@foo.org", PasswordHasher.hash("admin@foo.org", "blah"), 
				new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey(), authUserRec.getKey())));
		adminRepository.save(adminRecord);

		userRecord = new MongoAdminRecord(new AdminKey(), "user@foo.org", PasswordHasher.hash("user@foo.org", "blah"), 
				new HashSet<AuthorityKey>(Arrays.asList(authUserRec.getKey())));
		adminRepository.save(userRecord);
	}
	
	@Test
	public void loginExistingAdmin() {
		AdminRecord record = service.login("admin@foo.org", "blah");
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualTo(adminRecord.getKey());
		assertThat(record.getUserName()).isEqualTo(adminRecord.getUserName());
		assertThat(record.getAuthorities()).isNotEmpty();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()),
				new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
	}

	@Test
	public void loginExistingUser() {
		AdminRecord record = service.login("user@foo.org", "blah");
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualTo(userRecord.getKey());
		assertThat(record.getUserName()).isEqualTo(userRecord.getUserName());
		assertThat(record.getAuthorities()).isNotEmpty();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
	}

	@Test
	public void loginNonexistingAdmin() {
		AdminRecord record = service.login("admin2@foo.org", "blah");
		
		assertThat(record).isNull();
	}

	@Test
	public void loginExistingAdminWithWorngPassword() {
		AdminRecord record = service.login("admin@foo.org", "blah-blah");
		
		assertThat(record).isNull();
	}
}
