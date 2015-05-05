/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.*;

import java.util.Set;

import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAuthorityRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAuthorityRepository;
import org.flowninja.types.generic.AuthorityKey;
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
public class MongoAuthorityPersistenceServiceTest {
	@Autowired
	private MongoAuthorityPersistenceService service;
	
	@Autowired
	private IMongoAuthorityRepository repository;
	
	private AuthorityKey key;
	private MongoAuthorityRecord record;
	
	@Before
	public void prepareDatabase() {
		repository.findAll().forEach((n) -> repository.delete(n));
		
		key = new AuthorityKey();
		record = new MongoAuthorityRecord("dummy", key);
		
		repository.save(record);
	}
	
	@Test
	public void listRecords() {
		Set<AuthorityRecord> records = service.listAuthorities();

		assertThat(records).containsOnly(new AuthorityRecord("dummy", key));
	}
	
	@Test
	public void findExistingRecordByKey() {
		assertThat(service.findAuthorityByKey(key)).isEqualTo(new AuthorityRecord("dummy", key));
	}
	
	@Test
	public void findNonExistingRecordByKey() {
		assertThat(service.findAuthorityByKey(new AuthorityKey())).isNull();
	}

	@Test
	public void findExistingRecordByAuthority() {
		assertThat(service.findAuthorityByAuthority("dummy")).isEqualTo(new AuthorityRecord("dummy", key));
	}

	@Test
	public void findExistingRecordByAuthorityCamelCase() {
		assertThat(service.findAuthorityByAuthority("Dummy")).isEqualTo(new AuthorityRecord("dummy", key));
	}

	@Test
	public void findNonExistingRecordByAuthority() {
		assertThat(service.findAuthorityByAuthority("")).isNull();
	}
	
	@Test(expected=RecordAlreadyExistsException.class)
	public void insertExistingAuthority() {
		service.insertAuhority("dummy");
	}
	
	@Test(expected=RecordAlreadyExistsException.class)
	public void insertExistingAuthorityCamelCase() {
		service.insertAuhority("Dummy");
	}
	
	@Test
	public void insertNonExistingAuthority() {
		AuthorityRecord record = service.insertAuhority("foo");

		assertThat(record).isNotNull();
		assertThat(record.getAuthority()).isEqualTo("foo");
		assertThat(record.getKey()).isNotNull();
		
		MongoAuthorityRecord dbRecord = repository.findOne(record.getKey());
		
		assertThat(dbRecord).isNotNull();
		assertThat(dbRecord.getAuthority()).isEqualTo(record.getAuthority());
	}
	
	@Test
	public void deleteExistingAuthority() {
		service.deleteAuthority(key);

		assertThat(repository.findOne(key)).isNull();
	}
	
	@Test(expected=RecordNotFoundException.class)
	public void deleteNonExistingAuthority() {
		service.deleteAuthority(new AuthorityKey());
	}
}
