/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.ClientIDSecretGenerator;
import org.flowninja.persistence.generic.services.ICollectorPersistenceService;
import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoCollectorRecord;
import org.flowninja.persistence.mongodb.data.QMongoCollectorRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoCollectorRepository;
import org.flowninja.persistence.mongodb.repositories.IMongoAccountingGroupRepository;
import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class MongoCollectorPersistenceService implements ICollectorPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoCollectorPersistenceService.class);
	
	@Autowired
	private IMongoCollectorRepository collectorRepository;
	
	@Autowired
	private IMongoAccountingGroupRepository groupRepository;
	
	@Autowired
	private MongoAccountingGroupPersistenceService groupService;
	
	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#listCollectorsForAccountingGroup(org.flowninja.types.generic.AccountingGroupKey)
	 */
	@Override
	public Set<CollectorRecord> listCollectorsForAccountingGroup(AccountingGroupKey groupKey) {
		Set<CollectorRecord> collectors = new HashSet<CollectorRecord>();

		logger.info("listing collectors for accounting group {}", groupKey);
		
		AccountingGroupRecord groupRecord = groupService.findByKey(groupKey);
		
		if(groupRecord != null) {
			collectorRepository.findAll(QMongoCollectorRecord.mongoCollectorRecord.groupKey.eq(groupKey)).forEach((n) -> collectors.add(fromDB(n, groupRecord)));
		}
		
		return collectors;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#deleteCollectorsForAccountingGroup(org.flowninja.types.generic.AccountingGroupKey)
	 */
	@Override
	public void deleteCollectorsForAccountingGroup(AccountingGroupKey groupKey) {
		logger.info("deleting collectors for accounting group {}", groupKey);
		
		collectorRepository.findAll(QMongoCollectorRecord.mongoCollectorRecord.groupKey.eq(groupKey)).forEach((n) -> collectorRepository.delete(n));
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#findCollectoryByKey(org.flowninja.types.generic.CollectorKey)
	 */
	@Override
	public CollectorRecord findCollectoryByKey(CollectorKey key) {
		logger.info("finding collector for key {}", key);

		return fromDBConditionally(collectorRepository.findOne(key));
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#findCollectoryByName(java.lang.String)
	 */
	@Override
	public CollectorRecord findCollectoryByName(String name) {
		logger.info("finding collector for name {}", name);

		return fromDBConditionally(collectorRepository.findOne(QMongoCollectorRecord.mongoCollectorRecord.name.eq(name)));
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#findCollectoryByClientID(java.lang.String)
	 */
	@Override
	public CollectorRecord findCollectoryByClientID(String clientID) {
		logger.info("finding collector for client ID {}", clientID);

		return fromDBConditionally(collectorRepository.findOne(QMongoCollectorRecord.mongoCollectorRecord.clientID.eq(clientID)));
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#createCollector(org.flowninja.types.generic.AccountingGroupKey, java.lang.String, java.lang.String)
	 */
	@Override
	public CollectorRecord createCollector(AccountingGroupKey groupKey,	String name, String comment) throws RecordAlreadyExistsException, RecordNotFoundException {
		AccountingGroupRecord groupRecord = null;

		logger.info("Creating collector for group {} with name '{}' and comment '{}'", groupKey, name, comment);

		if((groupRecord = groupService.findByKey(groupKey)) == null) {
			logger.warn("Accounting group for key {} not found", groupKey);
			
			throw new RecordNotFoundException();
		}
		
		if(collectorRepository.findOne(QMongoCollectorRecord.mongoCollectorRecord.name.eq(name)) != null) {
			logger.warn("Collector with name '{}' already exists", name);

			throw new RecordAlreadyExistsException();
		}

		CollectorKey key = new CollectorKey();
		ClientIDSecretGenerator.IDSecretPair pair = ClientIDSecretGenerator.generatePair(groupKey, key);

		return fromDB(collectorRepository.save(new MongoCollectorRecord(key, groupKey, name, comment, pair.getClientID(), pair.getClientSecret())), groupRecord);
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#updateCollector(org.flowninja.persistence.generic.types.CollectorRecord)
	 */
	@Override
	public CollectorRecord updateCollector(CollectorRecord update) throws RecordAlreadyExistsException, RecordNotFoundException {
		AccountingGroupRecord groupRecord = null;
		MongoCollectorRecord record = null;

		logger.info("updating collector record {}", update);

		if((record = collectorRepository.findOne(update.getKey())) == null) {
			logger.warn("Collector for update {} not found", update);	
			
			throw new RecordNotFoundException();
		}
		
		if((groupRecord = groupService.findByKey(update.getAccountingGroup().getKey())) == null) {
			logger.warn("Accounting group for collector {} not found", update);
			
			throw new RecordNotFoundException();
		}

		boolean needsSave = false;
		
		if(!StringUtils.equals(record.getName(), update.getName())) {
			if(collectorRepository.findOne(QMongoCollectorRecord.mongoCollectorRecord.name.eq(update.getName())) != null) {
				logger.warn("Collector with name '{}' already exists", update.getName());

				throw new RecordAlreadyExistsException();
			}

			record.setName(update.getName());			
			needsSave = true;
		}

		if(!StringUtils.equals(record.getComment(), update.getComment())) {
			record.setComment(update.getComment());			
			needsSave = true;
		}

		if(needsSave)
			record = collectorRepository.save(record);
		
		return fromDB(record, groupRecord);
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#recreateCredentials(org.flowninja.types.generic.CollectorKey)
	 */
	@Override
	public CollectorRecord recreateCredentials(CollectorKey key) throws RecordNotFoundException {
		AccountingGroupRecord groupRecord = null;
		MongoCollectorRecord record = null;

		logger.info("recreating client credentials for collector {}", key);

		if((record = collectorRepository.findOne(key)) == null) {
			logger.warn("Collector for update {} not found", key);	
			
			throw new RecordNotFoundException();
		}
		
		if((groupRecord = groupService.findByKey(record.getGroupKey())) == null) {
			logger.warn("Accounting group for collector {} not found", key);
			
			throw new RecordNotFoundException();
		}
		
		ClientIDSecretGenerator.IDSecretPair pair = ClientIDSecretGenerator.generatePair(record.getGroupKey(), key);

		record.setClientID(pair.getClientID());
		record.setClientSecret(pair.getClientSecret());
		
		return fromDB(collectorRepository.save(record), groupRecord);
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.ICollectorPersistenceService#deleteCollector(org.flowninja.types.generic.CollectorKey)
	 */
	@Override
	public void deleteCollector(CollectorKey key) throws RecordNotFoundException {
		MongoCollectorRecord record = null;

		logger.info("deleting collector {}", key);

		if((record = collectorRepository.findOne(key)) == null) {
			logger.warn("Collector for update {} not found", key);	
			
			throw new RecordNotFoundException();
		}
		
		collectorRepository.delete(record);
	}

	private CollectorRecord fromDB(MongoCollectorRecord db, AccountingGroupRecord groupRecord) {
		return new CollectorRecord(db.getKey(), groupRecord, 
				db.getName(), db.getComment(), 
				db.getClientID(), db.getClientSecret(), 
				db.getCreatedWhen(), db.getLastModifiedAt());
	}

	private CollectorRecord fromDBConditionally(MongoCollectorRecord db) {
		CollectorRecord record = null;
		
		if(db != null) {
			AccountingGroupRecord groupRecord = groupService.findByKey(db.getGroupKey());
			
			if(groupRecord != null)
				record = fromDB(db, groupRecord);
		}
		
		return record;
	}

}
