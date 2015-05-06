/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAccountingGroupRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAccountingGroupRepository;
import org.flowninja.types.generic.AccountingGroupKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AssertThrows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfig.class})
public class MongoAccountingGroupPersistenceServiceTest {

	@Autowired
	private MongoAccountingGroupPersistenceService service;
	
	@Autowired
	private IMongoAccountingGroupRepository repository;
	
	private AccountingGroupKey keyOne;
	private MongoAccountingGroupRecord recordOne;
	private AccountingGroupKey keyTwo;
	private MongoAccountingGroupRecord recordTwo;
	
	@Before
	public void prepareDatabase() {
		repository.findAll().forEach((n) -> repository.delete(n));
		
		keyOne = new AccountingGroupKey();
		recordOne = repository.save(new MongoAccountingGroupRecord(keyOne, "dummyOne", "this is a dummy group"));
		keyTwo = new AccountingGroupKey();
		recordTwo = repository.save(new MongoAccountingGroupRecord(keyTwo, "dummyTwo", "this is a dummy group"));
		
		assertThat(recordOne.getCreatedWhen()).isNotNull();
		assertThat(recordOne.getLastModifiedAt()).isNotNull();
		assertThat(recordTwo.getCreatedWhen()).isNotNull();
		assertThat(recordTwo.getLastModifiedAt()).isNotNull();
	}

	@Test
	public void listAccoutingGroups() {
		Set<AccountingGroupRecord> records = service.listAccountingGroups();
		
		assertThat(records).containsOnly(new AccountingGroupRecord(recordOne.getKey(), recordOne.getName(), recordOne.getComment(), 
																	recordOne.getCreatedWhen(), recordOne.getLastModifiedAt()),
										new AccountingGroupRecord(recordTwo.getKey(), recordTwo.getName(), recordTwo.getComment(), 
												recordTwo.getCreatedWhen(),recordTwo.getLastModifiedAt()));
	}
	
	@Test
	public void findAccountingGroupByKeyKnownKey() {
		assertThat(service.findByKey(keyOne)).isEqualTo(new AccountingGroupRecord(recordOne.getKey(), recordOne.getName(), recordOne.getComment(), 
																	recordOne.getCreatedWhen(), recordOne.getLastModifiedAt()));
	}
	
	@Test
	public void findAccountingGroupByKeyUnknownKey() {
		assertThat(service.findByKey(new AccountingGroupKey())).isNull();
	}

	
	@Test
	public void findAccountingGroupByNameKnownName() {
		assertThat(service.findByName("dummyOne")).isEqualTo(new AccountingGroupRecord(recordOne.getKey(), recordOne.getName(), recordOne.getComment(), 
																	recordOne.getCreatedWhen(), recordOne.getLastModifiedAt()));
	}
	
	@Test
	public void findAccountingGroupByNameUnknownName() {
		assertThat(service.findByName("foo")).isNull();
	}
	
	@Test
	public void createAccountingGroupNewName() {
		AccountingGroupRecord record = service.createAccountingGroup("dummyThree", "yo ho ho");
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isNotNull();
		assertThat(record.getName()).isEqualTo("dummyThree");
		assertThat(record.getComment()).isEqualTo("yo ho ho");
		
		assertThat(service.findByKey(record.getKey())).isEqualTo(record);
	}
	
	@Test(expected=RecordAlreadyExistsException.class)
	public void createAccountingGroupExistingName() {
		service.createAccountingGroup("dummyOne", "foo");
	}
	
	@Test
	public void updateAccountingGroupNewComment() {
		AccountingGroupRecord record = service.findByKey(keyOne);
		
		assertThat(record).isNotNull();
		
		record.setComment("yo ho ho");
		
		AccountingGroupRecord newRecord = service.updateAccoutingGroup(record);
		
		assertThat(newRecord).isNotNull();
		assertThat(newRecord.getKey()).isEqualTo(record.getKey());
		assertThat(newRecord.getName()).isEqualTo(record.getName());
		assertThat(newRecord.getComment()).isEqualTo("yo ho ho");
		assertThat(newRecord.getCreatedWhen()).isEqualTo(record.getCreatedWhen());
		assertThat(newRecord.getLastModifiedAt()).isNotEqualTo(record.getLastModifiedAt());
		
		assertThat(service.findByKey(record.getKey())).isEqualTo(newRecord);
	}
		
	@Test
	public void updateAccountingGroupNewName() {
		AccountingGroupRecord record = service.findByKey(keyOne);
		
		assertThat(record).isNotNull();
		
		record.setComment("yo ho ho");
		record.setName("dummyFour");
		
		AccountingGroupRecord newRecord = service.updateAccoutingGroup(record);
		
		assertThat(newRecord).isNotNull();
		assertThat(newRecord.getKey()).isEqualTo(record.getKey());
		assertThat(newRecord.getName()).isEqualTo("dummyFour");
		assertThat(newRecord.getComment()).isEqualTo("yo ho ho");
		assertThat(newRecord.getCreatedWhen()).isEqualTo(record.getCreatedWhen());
		assertThat(newRecord.getLastModifiedAt()).isNotEqualTo(record.getLastModifiedAt());
		
		assertThat(service.findByKey(record.getKey())).isEqualTo(newRecord);
	}

	@Test(expected=RecordAlreadyExistsException.class)
	public void updateAccountingGroupExistingName() {
		AccountingGroupRecord record = service.findByKey(keyOne);
		
		assertThat(record).isNotNull();
		
		record.setComment("yo ho ho");
		record.setName("dummyTwo");
		
		service.updateAccoutingGroup(record);
	}

	@Test(expected=RecordNotFoundException.class)
	public void updateAccountingGroupUnknownKey() {
		service.updateAccoutingGroup(new AccountingGroupRecord(new AccountingGroupKey(), null, null, null, null));
	}

	@Test
	public void deleteAccountingGroupExistingGroup() {
		service.deleteAccountingGroup(keyOne);
		
		assertThat(service.findByKey(keyOne)).isNull();
	}

	@Test(expected=RecordNotFoundException.class)
	public void deleteAccountingGroupNonExistingGroup() {
		service.deleteAccountingGroup(new AccountingGroupKey());
	}
}
