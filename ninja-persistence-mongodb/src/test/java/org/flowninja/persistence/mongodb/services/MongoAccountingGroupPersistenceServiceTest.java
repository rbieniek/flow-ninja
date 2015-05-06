/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.mongodb.data.MongoAccountingGroupRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAccountingGroupRepository;
import org.flowninja.types.generic.AccountingGroupKey;
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
	
}
