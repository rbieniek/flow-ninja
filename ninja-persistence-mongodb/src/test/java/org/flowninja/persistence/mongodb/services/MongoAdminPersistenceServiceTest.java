/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.persistence.generic.PasswordHasher;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
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
	
	@Test
	public void findExistingUserByName() {
		AdminRecord record = service.findByUserName("user@foo.org");
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualTo(userRecord.getKey());
		assertThat(record.getUserName()).isEqualTo(userRecord.getUserName());
		assertThat(record.getAuthorities()).isNotEmpty();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
	}
	
	@Test
	public void findNonexistingAdminByName() {
		AdminRecord record = service.findByUserName("admin2@foo.org");
		
		assertThat(record).isNull();
	}
	
	@Test
	public void findExistingUserByKey() {
		AdminRecord record = service.findByKey(userRecord.getKey());
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualTo(userRecord.getKey());
		assertThat(record.getUserName()).isEqualTo(userRecord.getUserName());
		assertThat(record.getAuthorities()).isNotEmpty();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
	}
	
	@Test
	public void findNonexistingAdminByKey() {
		AdminRecord record = service.findByKey(new AdminKey());
		
		assertThat(record).isNull();
	}
	
	@Test
	public void listAdmins() {
		Set<AdminRecord> admins = service.listAdmins();

		assertThat(admins).containsOnly(new AdminRecord(adminRecord.getUserName(), adminRecord.getKey(), 
					new HashSet<AuthorityRecord>(Arrays.asList(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()), 
							new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey())))),
				new AdminRecord(userRecord.getUserName(), userRecord.getKey(), 
					new HashSet<AuthorityRecord>(Arrays.asList(new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey())))));
	}
	
	@Test(expected=RecordAlreadyExistsException.class)
	public void createAdminWithExistingName() {
		service.createAdmin(adminRecord.getUserName(),"blah", new HashSet<AuthorityKey>());
	}
	
	@Test
	public void createAdmin() {
		AdminRecord record = service.createAdmin("zoo@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey(), 
				authUserRec.getKey())));

		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()), 
				new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
		
		assertThat(service.login("zoo@foo", "blah-blah")).isEqualTo(record);
	}
	
	@Test
	public void createAdminWithUnknownAuthority() {
		AdminRecord record = service.createAdmin("zoo2@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey(), 
				authUserRec.getKey(),
				new AuthorityKey())));

		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo2@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()), 
				new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
		
		assertThat(service.login("zoo2@foo", "blah-blah")).isEqualTo(record);
	}
	
	@Test
	public void assignAuthority() {
		AdminRecord record = service.createAdmin("zoo3@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey())));

		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo3@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo3@foo", "blah-blah")).isEqualTo(record);

		record = service.assignAuthorities(record.getKey(), new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey(), 
				authUserRec.getKey())));
		
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo3@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()), 
				new AuthorityRecord(authUserRec.getAuthority(), authUserRec.getKey()));
		
		assertThat(service.login("zoo3@foo", "blah-blah")).isEqualTo(record);
	}
		
	@Test
	public void assignNonExistingAuthority() {
		AdminRecord record = service.createAdmin("zoo4@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey())));

		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo4@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo4@foo", "blah-blah")).isEqualTo(record);

		record = service.assignAuthorities(record.getKey(), new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey(), 
				new AuthorityKey())));
		
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo4@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo4@foo", "blah-blah")).isEqualTo(record);
	}
	
	@Test
	public void assignNullAuthorities() {
		AdminRecord record = service.createAdmin("zoo5@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey())));
	
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo5@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo5@foo", "blah-blah")).isEqualTo(record);
	
		record = service.assignAuthorities(record.getKey(), null);
		
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo5@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).isEmpty();
		
		assertThat(service.login("zoo5@foo", "blah-blah")).isEqualTo(record);
	}
	
	@Test
	public void assignEmptyAuthorities() {
		AdminRecord record = service.createAdmin("zoo6@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey())));
	
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo6@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo6@foo", "blah-blah")).isEqualTo(record);
	
		record = service.assignAuthorities(record.getKey(), new HashSet<AuthorityKey>());
		
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo6@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).isEmpty();
		
		assertThat(service.login("zoo6@foo", "blah-blah")).isEqualTo(record);
	}
	
	@Test(expected=RecordNotFoundException.class)
	public void deleteUnknownAdmin() {
		service.deleteAdmin(new AdminKey());
	}
	
	@Test
	public void deleteAdmin() {
		AdminRecord record = service.createAdmin("zoo7@foo","blah-blah", new HashSet<AuthorityKey>(Arrays.asList(authAdminRec.getKey())));
	
		assertThat(record).isNotNull();
		assertThat(record.getUserName()).isEqualTo("zoo7@foo");
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getAuthorities()).containsOnly(new AuthorityRecord(authAdminRec.getAuthority(), authAdminRec.getKey()));
		
		assertThat(service.login("zoo7@foo", "blah-blah")).isEqualTo(record);

		assertThat(service.findByKey(record.getKey())).isNotNull();

		service.deleteAdmin(record.getKey());
		
		assertThat(service.findByKey(record.getKey())).isNull();
	}
	
}
