/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.persistence.generic.ClientIDSecretGenerator;
import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAccountingGroupRecord;
import org.flowninja.persistence.mongodb.data.MongoCollectorRecord;
import org.flowninja.persistence.mongodb.data.QMongoCollectorRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAccountingGroupRepository;
import org.flowninja.persistence.mongodb.repositories.IMongoCollectorRepository;
import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
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
public class MongoCollectorPersistenceServiceTest {

	@Autowired
	private IMongoAccountingGroupRepository groupRepository;

	@Autowired
	private IMongoCollectorRepository collectorRepository;
	
	@Autowired
	private MongoCollectorPersistenceService  collectorService;
	
	private AccountingGroupKey groupKeyOne;
	private MongoAccountingGroupRecord groupRecordOne;
	private AccountingGroupKey groupKeyTwo;
	private MongoAccountingGroupRecord groupRecordTwo;
	private AccountingGroupKey groupKeyThree;
	
	private CollectorKey collectorKeyOne;
	private MongoCollectorRecord collectorRecordOne;
	private ClientIDSecretGenerator.IDSecretPair pairOne;
	private CollectorKey collectorKeyTwo;
	private MongoCollectorRecord collectorRecordTwo;
	private ClientIDSecretGenerator.IDSecretPair pairTwo;
	private CollectorKey collectorKeyThree;
	private MongoCollectorRecord collectorRecordThree;
	private ClientIDSecretGenerator.IDSecretPair pairThree;

	@Before
	public void prepareDatabase() {
		groupRepository.findAll().forEach((n) -> groupRepository.delete(n));
		
		groupKeyOne = new AccountingGroupKey();
		groupRecordOne = groupRepository.save(new MongoAccountingGroupRecord(groupKeyOne, "dummyOne", "this is a dummy group"));

		groupKeyTwo = new AccountingGroupKey();
		groupRecordTwo = groupRepository.save(new MongoAccountingGroupRecord(groupKeyTwo, "dummyTwo", "this is a dummy group"));
		
		groupKeyThree = new AccountingGroupKey();
		
		assertThat(groupRecordOne.getCreatedWhen()).isNotNull();
		assertThat(groupRecordOne.getLastModifiedAt()).isNotNull();
		assertThat(groupRecordTwo.getCreatedWhen()).isNotNull();
		assertThat(groupRecordTwo.getLastModifiedAt()).isNotNull();
		
		collectorRepository.findAll().forEach((n) -> collectorRepository.delete(n));

		collectorKeyOne = new CollectorKey();
		pairOne = ClientIDSecretGenerator.generatePair(groupKeyOne, collectorKeyOne);
		collectorRecordOne = collectorRepository.save(new MongoCollectorRecord(collectorKeyOne, groupKeyOne, 
				"dummyOne", "this is a dummy collector", 
				pairOne.getClientID(), pairOne.getClientSecret()));
		assertThat(collectorRecordOne.getCreatedWhen()).isNotNull();
		assertThat(collectorRecordOne.getLastModifiedAt()).isNotNull();
		
		collectorKeyTwo = new CollectorKey();
		pairTwo = ClientIDSecretGenerator.generatePair(groupKeyTwo, collectorKeyTwo);
		collectorRecordTwo = collectorRepository.save(new MongoCollectorRecord(collectorKeyTwo, groupKeyTwo, 
				"dummyTwo", "this is a dummy collector", 
				pairTwo.getClientID(), pairTwo.getClientSecret()));
		assertThat(collectorRecordTwo.getCreatedWhen()).isNotNull();
		assertThat(collectorRecordTwo.getLastModifiedAt()).isNotNull();

		collectorKeyThree = new CollectorKey();
		pairThree = ClientIDSecretGenerator.generatePair(groupKeyThree, collectorKeyThree);
		collectorRecordThree = collectorRepository.save(new MongoCollectorRecord(collectorKeyThree, groupKeyThree, 
				"dummyThree", "this is a dummy collector", 
				pairThree.getClientID(), pairThree.getClientSecret()));
		assertThat(collectorRecordThree.getCreatedWhen()).isNotNull();
		assertThat(collectorRecordThree.getLastModifiedAt()).isNotNull();
	}

	@Test
	public void listCollectorsForAccountingGroup() {
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).containsOnly(fromDB(collectorRecordOne));
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyTwo)).containsOnly(fromDB(collectorRecordTwo));
	}

	@Test
	public void listCollectorsForAccountingGroupUnknown() {
		assertThat(collectorService.listCollectorsForAccountingGroup(new AccountingGroupKey())).isEmpty();
	}

	@Test
	public void listCollectorsForAccountingGroupOrphaned() {
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyThree)).isEmpty();
	}

	@Test
	public void deleteCollectorsForAccountingGroup() {
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).containsOnly(fromDB(collectorRecordOne));
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyTwo)).containsOnly(fromDB(collectorRecordTwo));
		
		collectorService.deleteCollectorsForAccountingGroup(groupKeyOne);

		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).isEmpty();
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyTwo)).containsOnly(fromDB(collectorRecordTwo));
	}
	
	@Test
	public void deleteCollectorsForAccountingGroupUnknown() {
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).containsOnly(fromDB(collectorRecordOne));
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyTwo)).containsOnly(fromDB(collectorRecordTwo));
		
		collectorService.deleteCollectorsForAccountingGroup(new AccountingGroupKey());

		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).containsOnly(fromDB(collectorRecordOne));
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyTwo)).containsOnly(fromDB(collectorRecordTwo));
	}

	@Test
	public void deleteCollectorsForAccountingGroupOrphaned() {
		assertThat(collectorRepository.findAll(QMongoCollectorRecord.mongoCollectorRecord.groupKey.eq(groupKeyThree))).isNotEmpty();
		
		collectorService.deleteCollectorsForAccountingGroup(groupKeyThree);

		assertThat(collectorRepository.findAll(QMongoCollectorRecord.mongoCollectorRecord.groupKey.eq(groupKeyThree))).isEmpty();
	}
	
	@Test
	public void findByKey() {
		assertThat(collectorService.findCollectoryByKey(collectorKeyOne)).isEqualTo(fromDB(collectorRecordOne));
		assertThat(collectorService.findCollectoryByKey(collectorKeyTwo)).isEqualTo(fromDB(collectorRecordTwo));
	}

	@Test
	public void findByKeyUnknown() {
		assertThat(collectorService.findCollectoryByKey(new CollectorKey())).isNull();
	}

	@Test
	public void findByKeyOrphaned() {
		assertThat(collectorService.findCollectoryByKey(collectorKeyThree)).isNull();
	}

	@Test
	public void findByName() {
		assertThat(collectorService.findCollectoryByName(collectorRecordOne.getName())).isEqualTo(fromDB(collectorRecordOne));
		assertThat(collectorService.findCollectoryByName(collectorRecordTwo.getName())).isEqualTo(fromDB(collectorRecordTwo));
	}

	@Test
	public void findByNameUnknown() {
		assertThat(collectorService.findCollectoryByName("foo")).isNull();
	}

	@Test
	public void findByNameOrphaned() {
		assertThat(collectorService.findCollectoryByName(collectorRecordThree.getName())).isNull();
	}

	@Test
	public void findByClientID() {
		assertThat(collectorService.findCollectoryByClientID(collectorRecordOne.getClientID())).isEqualTo(fromDB(collectorRecordOne));
		assertThat(collectorService.findCollectoryByClientID(collectorRecordTwo.getClientID())).isEqualTo(fromDB(collectorRecordTwo));
	}

	@Test
	public void findByClientIDUnknown() {
		assertThat(collectorService.findCollectoryByClientID("foo")).isNull();
	}

	@Test
	public void findByClientIDOrphaned() {
		assertThat(collectorService.findCollectoryByClientID(collectorRecordThree.getClientID())).isNull();
	}

	@Test
	public void createCollector() {
		CollectorRecord record = collectorService.createCollector(groupKeyOne, "dummyFour", collectorRecordOne.getComment());
		
		assertThat(record).isNotNull();
		
		assertThat(collectorService.listCollectorsForAccountingGroup(groupKeyOne)).containsOnly(record, fromDB(collectorRecordOne));
	}

	@Test(expected=RecordNotFoundException.class)
	public void createCollectorUnkownGroup() {
		collectorService.createCollector(new AccountingGroupKey(), "dummyFive", "foo");
	}

	@Test(expected=RecordNotFoundException.class)
	public void createCollectorOrphanedGroup() {
		collectorService.createCollector(groupKeyThree, "dummySix", "foo");
	}

	@Test(expected=RecordAlreadyExistsException.class)
	public void createCollectorDuplicateName() {
		collectorService.createCollector(groupKeyOne, collectorRecordOne.getName(), "foo");		
	}
	
	@Test
	public void updateCollectorName() {
		CollectorRecord record = collectorService.updateCollector(new CollectorRecord(collectorKeyOne, fromDB(groupRecordOne), 
				"dummySeven", collectorRecordOne.getComment(), 
				collectorRecordOne.getClientID(), collectorRecordOne.getClientSecret(), 
				collectorRecordOne.getCreatedWhen(), collectorRecordOne.getLastModifiedAt()));
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualsToByComparingFields(collectorKeyOne);
		assertThat(record.getAccountingGroup()).isEqualTo(fromDB(groupRecordOne));
		assertThat(record.getName()).isEqualTo("dummySeven");
		assertThat(record.getComment()).isEqualTo(collectorRecordOne.getComment());
		assertThat(record.getClientId()).isEqualTo(collectorRecordOne.getClientID());
		assertThat(record.getClientSecret()).isEqualTo(collectorRecordOne.getClientSecret());
		assertThat(record.getCreatedWhen()).isEqualTo(collectorRecordOne.getCreatedWhen());
		assertThat(record.getLastModifiedAt()).isNotEqualTo(collectorRecordOne.getLastModifiedAt());
	}

	@Test
	public void updateCollectorComment() {
		CollectorRecord record = collectorService.updateCollector(new CollectorRecord(collectorKeyOne, fromDB(groupRecordOne), 
				collectorRecordOne.getName(), "bogus", 
				collectorRecordOne.getClientID(), collectorRecordOne.getClientSecret(), 
				collectorRecordOne.getCreatedWhen(), collectorRecordOne.getLastModifiedAt()));
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualsToByComparingFields(collectorKeyOne);
		assertThat(record.getAccountingGroup()).isEqualTo(fromDB(groupRecordOne));
		assertThat(record.getName()).isEqualTo(collectorRecordOne.getName());
		assertThat(record.getComment()).isEqualTo("bogus");
		assertThat(record.getClientId()).isEqualTo(collectorRecordOne.getClientID());
		assertThat(record.getClientSecret()).isEqualTo(collectorRecordOne.getClientSecret());
		assertThat(record.getCreatedWhen()).isEqualTo(collectorRecordOne.getCreatedWhen());
		assertThat(record.getLastModifiedAt()).isNotEqualTo(collectorRecordOne.getLastModifiedAt());		
	}

	@Test
	public void updateCollectorNoChanges() {
		CollectorRecord record = collectorService.updateCollector(fromDB(collectorRecordOne));
		
		assertThat(record).isEqualTo(fromDB(collectorRecordOne));
	}

	@Test(expected=RecordNotFoundException.class)
	public void updateCollectorUnknown() {
		collectorService.updateCollector(new CollectorRecord(new CollectorKey(), fromDB(groupRecordOne), 
				"dummySeven", collectorRecordOne.getComment(), 
				collectorRecordOne.getClientID(), collectorRecordOne.getClientSecret(), 
				collectorRecordOne.getCreatedWhen(), collectorRecordOne.getLastModifiedAt()));
	}

	@Test(expected=RecordNotFoundException.class)
	public void updateCollectorOrphaned() {
		collectorService.updateCollector(new CollectorRecord(collectorKeyThree, new AccountingGroupRecord(groupKeyThree, "foo", "foo", null, null), 
				"dummySeven", collectorRecordOne.getComment(), 
				collectorRecordOne.getClientID(), collectorRecordOne.getClientSecret(), 
				collectorRecordOne.getCreatedWhen(), collectorRecordOne.getLastModifiedAt()));		
	}
	
	@Test(expected=RecordAlreadyExistsException.class)
	public void updateCollectorNameAlreadyExists() {
		collectorService.updateCollector(new CollectorRecord(collectorKeyOne, fromDB(groupRecordOne), 
				collectorRecordTwo.getName(), collectorRecordOne.getComment(), 
				collectorRecordOne.getClientID(), collectorRecordOne.getClientSecret(), 
				collectorRecordOne.getCreatedWhen(), collectorRecordOne.getLastModifiedAt()));		
	}
	
	@Test
	public void recreateCredentials() {
		CollectorRecord record = collectorService.recreateCredentials(collectorKeyOne);
		
		assertThat(record).isNotNull();
		assertThat(record.getKey()).isEqualsToByComparingFields(collectorKeyOne);
		assertThat(record.getAccountingGroup()).isEqualTo(fromDB(groupRecordOne));
		assertThat(record.getName()).isEqualTo(collectorRecordOne.getName());
		assertThat(record.getComment()).isEqualTo(collectorRecordOne.getComment());
		assertThat(record.getClientId()).isNotEqualTo(collectorRecordOne.getClientID());
		assertThat(record.getClientSecret()).isNotEqualTo(collectorRecordOne.getClientSecret());
		assertThat(record.getCreatedWhen()).isEqualTo(collectorRecordOne.getCreatedWhen());
		assertThat(record.getLastModifiedAt()).isNotEqualTo(collectorRecordOne.getLastModifiedAt());		
	}

	@Test(expected=RecordNotFoundException.class)
	public void recreateCredentialsUnknown() {
		collectorService.recreateCredentials(new CollectorKey());
	}

	@Test(expected=RecordNotFoundException.class)
	public void recreateCredentialsOrphaned() {
		collectorService.recreateCredentials(collectorKeyThree);
	}

	@Test
	public void deleteCollector() {
		assertThat(collectorRepository.findOne(collectorKeyOne)).isNotNull();
		
		collectorService.deleteCollector(collectorKeyOne);
		
		assertThat(collectorRepository.findOne(collectorKeyOne)).isNull();		
	}

	@Test(expected=RecordNotFoundException.class)
	public void deleteCollectorUnknown() {
		collectorService.deleteCollector(new CollectorKey());
	}
	
	@Test
	public void deleteCollectorOrphaned() {
		assertThat(collectorRepository.findOne(collectorKeyThree)).isNotNull();
		
		collectorService.deleteCollector(collectorKeyThree);
		
		assertThat(collectorRepository.findOne(collectorKeyThree)).isNull();		
	}

	private CollectorRecord fromDB(MongoCollectorRecord db) {
		return new CollectorRecord(db.getKey(), fromDB(groupRepository.findOne(db.getGroupKey())), 
					db.getName(), db.getComment(), 
					db.getClientID(), db.getClientSecret(), 
					db.getCreatedWhen(), db.getLastModifiedAt());
	}
	
	private AccountingGroupRecord fromDB(MongoAccountingGroupRecord db) {
		if(db == null)
			return null;
		
		return new AccountingGroupRecord(db.getKey(), db.getName(), db.getComment(), db.getCreatedWhen(), db.getLastModifiedAt());
	}
}
