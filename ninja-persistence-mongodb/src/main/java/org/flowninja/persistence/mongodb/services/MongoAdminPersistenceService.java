/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.PasswordHasher;
import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoAdminRecord;
import org.flowninja.persistence.mongodb.data.MongoAuthorityRecord;
import org.flowninja.persistence.mongodb.data.QMongoAdminRecord;
import org.flowninja.persistence.mongodb.data.QMongoAuthorityRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoAdminRepository;
import org.flowninja.persistence.mongodb.repositories.IMongoAuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;

/**
 * @author rainer
 *
 */
@Service
public class MongoAdminPersistenceService implements IAdminPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoAdminPersistenceService.class);

	@Autowired
	private IMongoAdminRepository adminRepository;

	@Autowired
	private IMongoAuthorityRepository authorityRepository;

	@Override
	public AdminRecord login(String userName, String password) {
		AdminRecord record = null;
		
		logger.info("login for user: {}", userName);

		userName = StringUtils.lowerCase(userName);

		MongoAdminRecord dbRecord = adminRepository.findOne((new BooleanBuilder())
				.and(QMongoAdminRecord.mongoAdminRecord.userName.eq(userName))
				.and(QMongoAdminRecord.mongoAdminRecord.passwordHash.eq(PasswordHasher.hash(userName, password))));
		
		if(dbRecord != null) 
			record = convertDbRecord(dbRecord);
		
		logger.info("login for user {} yielded {}", userName, record);
		
		return record;
	}

	@Override
	public AdminRecord findByUserName(String userName) {
		AdminRecord record = null;
		
		logger.info("find user by name: {}", userName);
		
		MongoAdminRecord dbRecord = adminRepository.findOne(QMongoAdminRecord.mongoAdminRecord.userName.eq(StringUtils.lowerCase(userName)));
		
		if(dbRecord != null) 
			record = convertDbRecord(dbRecord);
		
		logger.info("finding user by name {} yielded {}", userName, record);
		
		return record;
	}

	@Override
	public AdminRecord findByKey(AdminKey key) {
		AdminRecord record = null;
		
		logger.info("find user by key: {}", key);
		
		MongoAdminRecord dbRecord = adminRepository.findOne(QMongoAdminRecord.mongoAdminRecord.key.eq(key));
		
		if(dbRecord != null) 
			record = convertDbRecord(dbRecord);
		
		logger.info("finding user by key {} yielded {}", key, record);
		
		return record;
	}

	@Override
	public Set<AdminRecord> listAdmins() {
		Set<AdminRecord> records = new HashSet<AdminRecord>();
		
		adminRepository.findAll().forEach((n) -> records.add(convertDbRecord(n)));
		
		return records;
	}

	@Override
	public AdminRecord createAdmin(String userName, String password, Set<AuthorityKey> authorities) throws RecordAlreadyExistsException {
		logger.info("creating user with name: {}", userName);

		userName = StringUtils.lowerCase(userName);

		if(adminRepository.findOne(QMongoAdminRecord.mongoAdminRecord.userName.eq(userName)) != null) {
			logger.warn("admin already exists, name={}", userName);
			
			throw new RecordAlreadyExistsException();
		}
		
		Set<MongoAuthorityRecord> dbAuthorityRecords = findAuthorityRecords(authorities);

		MongoAdminRecord record = new MongoAdminRecord(new AdminKey(), userName, PasswordHasher.hash(userName, password), 
				dbAuthorityRecords.stream().map((n) -> n.getKey()).collect(Collectors.toSet()));
		
		logger.info("creating user with name {} yielded {}", userName, record);
		
		return convertDbRecord(adminRepository.save(record));
	}

	@Override
	public AdminRecord assignAuthorities(AdminKey key, Set<AuthorityKey> authorities) throws RecordNotFoundException {
		logger.info("updating authorities for ", key);

		MongoAdminRecord dbRecord = adminRepository.findOne(QMongoAdminRecord.mongoAdminRecord.key.eq(key));

		if(dbRecord == null) {
			logger.warn("No admin record found for key {}", key);
			
			throw new RecordNotFoundException();
		}
		
		dbRecord.setAuthorities(findAuthorityRecords(authorities).stream().map((n) -> n.getKey()).collect(Collectors.toSet()));
		
		return convertDbRecord(adminRepository.save(dbRecord));
	}

	@Override
	public void deleteAdmin(AdminKey key) throws RecordNotFoundException {
		logger.info("deleting admin with key {}", key);
		
		MongoAdminRecord record = adminRepository.findOne(key);
		
		if(record == null) {
			logger.info("no admin record found for key {}", key);
			
			throw new RecordNotFoundException();
		}
		
		adminRepository.delete(record);
	}
	
	private Set<MongoAuthorityRecord> findAuthorityRecords(Set<AuthorityKey> keys) {
		HashSet<MongoAuthorityRecord> records = new HashSet<MongoAuthorityRecord>();

		if(!CollectionUtils.isEmpty(keys)) {
			BooleanBuilder builder = new BooleanBuilder();
	
			keys.forEach((n) -> builder.or(QMongoAuthorityRecord.mongoAuthorityRecord.key.eq(n)));
	
			authorityRepository.findAll(builder).forEach((n) -> records.add(n));
		}
		
		return records;
	}
	
	private AdminRecord convertDbRecord(MongoAdminRecord dbRecord) {
		Set<AuthorityRecord> authorities = new HashSet<AuthorityRecord>();
		
		if(!CollectionUtils.isEmpty(dbRecord.getAuthorities())) {
			BooleanBuilder builder = new BooleanBuilder();
			
			dbRecord.getAuthorities().forEach((n) -> builder.or(QMongoAuthorityRecord.mongoAuthorityRecord.key.eq(n)));
			
			authorityRepository.findAll(builder).forEach((n) -> authorities.add(new AuthorityRecord(n.getAuthority(), n.getKey())));
		}
		
		return new AdminRecord(dbRecord.getUserName(), dbRecord.getKey(), authorities);
	}
}
