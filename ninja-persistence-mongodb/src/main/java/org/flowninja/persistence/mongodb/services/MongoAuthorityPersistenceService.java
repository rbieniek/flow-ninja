/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.IAuthorityPersistenceService;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAuthorityRecord;
import org.flowninja.persistence.mongodb.data.QMongoAuthorityRecord;
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
public class MongoAuthorityPersistenceService implements IAuthorityPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoAuthorityPersistenceService.class);

	@Autowired
	private IMongoAuthorityRepository repository;
	
	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IAuthorityPersistenceService#listAuthorities()
	 */
	@Override
	public Set<AuthorityRecord> listAuthorities() {		
		Set<AuthorityRecord> records = new HashSet<AuthorityRecord>();
		
		logger.info("listing authority records");
		
		repository.findAll().forEach((n) -> records.add(new AuthorityRecord(n.getAuthority(), n.getKey())));
		
		return records;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IAuthorityPersistenceService#findAuthorityByKey(org.flowninja.persistence.generic.types.AuthorityKey)
	 */
	@Override
	public AuthorityRecord findAuthorityByKey(AuthorityKey key) {
		AuthorityRecord record = null;
		
		logger.info("looking up authority record, key={}", key);
		
		MongoAuthorityRecord dbRecord = repository.findOne(key);

		if(dbRecord != null)
			record = new AuthorityRecord(dbRecord.getAuthority(), dbRecord.getKey());

		logger.info("looking up authority record yieled, key={}: {}", key, record);

		return record;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IAuthorityPersistenceService#findAuthorityByAuthority(java.lang.String)
	 */
	@Override
	public AuthorityRecord findAuthorityByAuthority(String authority) {
		AuthorityRecord record = null;
		
		logger.info("looking up authority record, authority={}", authority);
		
		authority = StringUtils.lowerCase(authority);		

		MongoAuthorityRecord dbRecord = repository.findOne(QMongoAuthorityRecord.mongoAuthorityRecord.authority.eq(authority));

		if(dbRecord != null)
			record = new AuthorityRecord(dbRecord.getAuthority(), dbRecord.getKey());

		logger.info("looking up authority record yieled, authority={}: {}", authority, record);

		return record;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IAuthorityPersistenceService#insertAuhority(java.lang.String)
	 */
	@Override
	public AuthorityRecord insertAuhority(String authority) throws RecordAlreadyExistsException {
		logger.info("creating new authority, authority={}", authority);

		authority = StringUtils.lowerCase(authority);		

		if(repository.findOne(QMongoAuthorityRecord.mongoAuthorityRecord.authority.eq(authority)) != null) {
			logger.warn("authority already exists, authority={}", authority);
			
			throw new RecordAlreadyExistsException();
		}

		MongoAuthorityRecord dbRecord = repository.save(new MongoAuthorityRecord(authority, new AuthorityKey()));

		logger.info("created new authority, authority={}, key={}", dbRecord.getAuthority(), dbRecord.getKey());

		return new AuthorityRecord(dbRecord.getAuthority(), dbRecord.getKey());
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IAuthorityPersistenceService#deleteAuthority(org.flowninja.persistence.generic.types.AuthorityKey)
	 */
	@Override
	public void deleteAuthority(AuthorityKey key) throws RecordNotFoundException {
		logger.info("deleting authority, key={}", key);
		
		MongoAuthorityRecord record = repository.findOne(key);
		
		if(record == null) {
			logger.info("failed to delete non-existing authority, key={}", key);
			
			throw new RecordNotFoundException();
		}
		
		repository.delete(record);
	}

}
