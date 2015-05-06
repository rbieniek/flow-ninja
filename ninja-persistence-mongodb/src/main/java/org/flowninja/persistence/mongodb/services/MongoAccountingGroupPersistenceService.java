/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.IAccountingGroupPersistenceService;
import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAccountingGroupRecord;
import org.flowninja.persistence.mongodb.data.QMongoAccountingGroupRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAccountingGroupRepository;
import org.flowninja.types.generic.AccountingGroupKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class MongoAccountingGroupPersistenceService implements IAccountingGroupPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoAccountingGroupPersistenceService.class);
	
	@Autowired
	private IMongoAccountingGroupRepository repository;
	
	@Override
	public Set<AccountingGroupRecord> listAccountingGroups() {
		Set<AccountingGroupRecord> records = new HashSet<AccountingGroupRecord>();
		
		logger.info("listing all accounting group records");
		
		repository.findAll().forEach((n) -> records.add(new AccountingGroupRecord(n.getKey(), n.getName(), n.getComment(), n.getCreatedWhen(), n.getLastModifiedAt())));
		
		return records;
	}

	@Override
	public AccountingGroupRecord findByKey(AccountingGroupKey key) {
		AccountingGroupRecord acr = null;
		MongoAccountingGroupRecord macr = null;
		
		logger.info("finding accouting group record: key {}", key);
		
		if((macr = repository.findOne(key)) != null)
			acr = new AccountingGroupRecord(macr.getKey(), macr.getName(), macr.getComment(), macr.getCreatedWhen(), macr.getLastModifiedAt());
		
		logger.info("finding accouting group record yielded: key {}, record {}", key, acr);		
		
		return acr;
	}

	@Override
	public AccountingGroupRecord findByName(String name) {
		AccountingGroupRecord acr = null;
		MongoAccountingGroupRecord macr = null;
		
		logger.info("finding accouting group record: name {}", name);
		
		if((macr = repository.findOne(QMongoAccountingGroupRecord.mongoAccountingGroupRecord.name.eq(name))) != null)
			acr = new AccountingGroupRecord(macr.getKey(), macr.getName(), macr.getComment(), macr.getCreatedWhen(), macr.getLastModifiedAt());
		
		logger.info("finding accouting group record yielded: name {}, record {}", name, acr);		
		
		return acr;
	}

	@Override
	public AccountingGroupRecord createAccountingGroup(String name, String comment) throws RecordAlreadyExistsException {
		MongoAccountingGroupRecord macr = null;

		logger.info("creating accouting group record: name {}, comment {}", name, comment);

		if(repository.findOne(QMongoAccountingGroupRecord.mongoAccountingGroupRecord.name.eq(name)) != null) {
			logger.error("Accouting group with name {} already exists", name);
			
			throw new RecordAlreadyExistsException();
		}

		macr = repository.save(new MongoAccountingGroupRecord(new AccountingGroupKey(), name, comment));
		
		return new AccountingGroupRecord(macr.getKey(), macr.getName(), macr.getComment(), macr.getCreatedWhen(), macr.getLastModifiedAt());
	}

	@Override
	public AccountingGroupRecord updateAccoutingGroup(AccountingGroupRecord record) throws RecordNotFoundException, RecordAlreadyExistsException {
		MongoAccountingGroupRecord macr = null;

		logger.info("updating accouting group record: record {}", record);

		if((macr = repository.findOne(record.getKey())) == null) {
			logger.error("Accouting group with key {} not found", record.getKey());
			
			throw new RecordNotFoundException();
		}

		if(!StringUtils.equals(record.getName(), macr.getName())) {
			if(repository.findOne(QMongoAccountingGroupRecord.mongoAccountingGroupRecord.name.eq(record.getName())) != null) {
				logger.error("Accouting group with name {} already exists", record.getName());
				
				throw new RecordAlreadyExistsException();
			}
			
			macr.setName(record.getName());
		}

		macr.setComment(record.getComment());

		macr = repository.save(macr);

		return new AccountingGroupRecord(macr.getKey(), macr.getName(), macr.getComment(), macr.getCreatedWhen(), macr.getLastModifiedAt());
	}

	@Override
	public void deleteAccountingGroup(AccountingGroupKey key)throws RecordNotFoundException {
		MongoAccountingGroupRecord macr = null;

		logger.info("deleting accouting group record: key {}", key);

		if((macr = repository.findOne(key)) == null) {
			logger.error("Accouting group with key {} not found", key);
			
			throw new RecordNotFoundException();
		}
		
		repository.delete(macr);
	}

}
