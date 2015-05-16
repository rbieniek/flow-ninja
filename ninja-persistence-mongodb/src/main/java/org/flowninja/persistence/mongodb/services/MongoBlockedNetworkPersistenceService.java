/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService;
import org.flowninja.persistence.generic.types.BlockedNetworkRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoBlockedNetworkRecord;
import org.flowninja.persistence.mongodb.data.QMongoBlockedNetworkRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoBlockedNetworkRepository;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.flowninja.types.net.CIDR4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;

/**
 * @author rainer
 *
 */
@Service
public class MongoBlockedNetworkPersistenceService implements IBlockedNetworksPersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(MongoBlockedNetworkPersistenceService.class);
	
	@Autowired
	private IMongoBlockedNetworkRepository repository;
	
	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService#listBlockedNetworks()
	 */
	@Override
	public Set<BlockedNetworkRecord> listBlockedNetworks() {
		Set<BlockedNetworkRecord> records = new HashSet<BlockedNetworkRecord>();
		
		logger.info("listing all blocked networks");

		repository.findAll().forEach((n) -> records.add(convertFromDbRecord(n)));
		
		return records;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService#findBlockedNetwork(org.flowninja.types.net.CIDR4Address)
	 */
	@Override
	public BlockedNetworkRecord findBlockedNetwork(CIDR4Address address) {
		BlockedNetworkRecord record = null;
		
		logger.info("looking up blocked network record for CIDR address {}", address);

		MongoBlockedNetworkRecord dbRecord = repository.findOne(QMongoBlockedNetworkRecord.mongoBlockedNetworkRecord.cidr.eq(cidrAddressToDbValue(address)));
		
		if(dbRecord != null)
			record = convertFromDbRecord(dbRecord);
		
		logger.info("looking up blocked network record for CIDR address {} yielded {}", address, record);

		return record;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService#findBlockedNetwork(org.flowninja.types.generic.BlockedNetworkKey)
	 */
	@Override
	public BlockedNetworkRecord findBlockedNetwork(BlockedNetworkKey key) {
		BlockedNetworkRecord record = null;
		
		logger.info("looking up blocked network record for key {}", key);

		MongoBlockedNetworkRecord dbRecord = repository.findOne(key);
		
		if(dbRecord != null)
			record = convertFromDbRecord(dbRecord);
		
		logger.info("looking up blocked network record for key {} yielded {}", key, record);

		return record;
	}

	@Override
	public BlockedNetworkRecord findContainingBlockedNetwork(CIDR4Address address) {
		BlockedNetworkRecord record = null;
		
		logger.info("looking up matching blocked network record for CIDR host address {}", address);

		if(address.getNetmask() == 32) {
			ArrayList<BooleanExpression> parts = new ArrayList<BooleanExpression>();
			
			for(int i=8; i<32; i++) {
				parts.add(QMongoBlockedNetworkRecord.mongoBlockedNetworkRecord.cidr.eq(cidrAddressToDbValue(new CIDR4Address(address.getAddress(), i))));
			}
			
		MongoBlockedNetworkRecord dbRecord = repository.findOne(BooleanExpression.anyOf(parts.toArray(new BooleanExpression[0])));
		
		if(dbRecord != null)
			record = convertFromDbRecord(dbRecord);
		}

		logger.info("looking up matching blocked network record for CIDR host address {} yielded {}", address, record);

		return record;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService#addBlockedNetwork(org.flowninja.types.net.CIDR4Address)
	 */
	@Override
	public BlockedNetworkRecord addBlockedNetwork(CIDR4Address address) throws RecordAlreadyExistsException {
		BlockedNetworkRecord record = null;
		
		logger.info("adding blocked network record for CIDR address {}", address);

		if(repository.findOne(QMongoBlockedNetworkRecord.mongoBlockedNetworkRecord.cidr.eq(cidrAddressToDbValue(address))) != null) {
			logger.warn("CIDR address {} already in database", address);
			
			throw new RecordAlreadyExistsException();
		}
		
		record = convertFromDbRecord(repository.save(new MongoBlockedNetworkRecord(new BlockedNetworkKey(), cidrAddressToDbValue(address))));
		
		logger.info("adding blocked network record for CIDR address {} yielded {}", address, record);

		return record;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService#deleteBlockedNetwork(org.flowninja.types.generic.BlockedNetworkKey)
	 */
	@Override
	public void deleteBlockedNetwork(BlockedNetworkKey key) throws RecordNotFoundException {
		logger.info("deleting blocked network record for key {}", key);

		MongoBlockedNetworkRecord dbRecord;
		
		if((dbRecord = repository.findOne(key)) == null) {
			logger.info("no database record for key {}", key);
			
			throw new RecordNotFoundException();
		}

		repository.delete(dbRecord);
	}

	private BlockedNetworkRecord convertFromDbRecord(MongoBlockedNetworkRecord dbRecord) {
		byte[] addr = new byte[4];

		System.arraycopy(dbRecord.getCidr(), 0, addr, 0, 4);
		
		return new BlockedNetworkRecord(dbRecord.getKey(), new CIDR4Address(addr, dbRecord.getCidr()[4]));
	}

	private byte[] cidrAddressToDbValue(CIDR4Address cidr) {
		byte[] rep = new byte[5];
		
		System.arraycopy(cidr.getAddress(), 0, rep, 0, 4);
		rep[4] = (byte)(cidr.getNetmask());
		
		return rep;
	}
}
