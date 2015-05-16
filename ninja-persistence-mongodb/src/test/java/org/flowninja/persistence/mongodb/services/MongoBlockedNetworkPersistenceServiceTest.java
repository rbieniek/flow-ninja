/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.persistence.generic.types.BlockedNetworkRecord;
import org.flowninja.persistence.generic.types.RecordAlreadyExistsException;
import org.flowninja.persistence.generic.types.RecordNotFoundException;
import org.flowninja.persistence.mongodb.data.MongoBlockedNetworkRecord;
import org.flowninja.persistence.mongodb.repositories.IMongoBlockedNetworkRepository;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.flowninja.types.net.CIDR4Address;
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
public class MongoBlockedNetworkPersistenceServiceTest {

	@Autowired
	private IMongoBlockedNetworkRepository repository;

	@Autowired
	private MongoBlockedNetworkPersistenceService service;
	
	private BlockedNetworkKey key_192_168_0_0_16;
	private MongoBlockedNetworkRecord record_192_168_0_0_16;
	
	@Before
	public void before() {
		repository.findAll().forEach((n) -> repository.delete(n));

		key_192_168_0_0_16 = new BlockedNetworkKey();		
		
		record_192_168_0_0_16 = repository.save(new MongoBlockedNetworkRecord(key_192_168_0_0_16, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00, (byte)0x10 }));
		
		assertThat(record_192_168_0_0_16).isNotNull();
		assertThat(record_192_168_0_0_16.getKey()).isEqualTo(key_192_168_0_0_16);
	}
	
	@Test
	public void listBlockeNetworks() {
		assertThat(service.listBlockedNetworks()).containsOnly(new BlockedNetworkRecord(key_192_168_0_0_16, 
				new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16)));
	}
	
	@Test
	public void findKnownByAddress() {
		assertThat(service.findBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16)))
			.isEqualTo(new BlockedNetworkRecord(key_192_168_0_0_16, 
				new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16)));
	}
	
	@Test
	public void findKnownByKey() {
		assertThat(service.findBlockedNetwork(key_192_168_0_0_16))
			.isEqualTo(new BlockedNetworkRecord(key_192_168_0_0_16, 
				new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16)));
	}
	
	@Test
	public void findUnknownByAddress() {
		assertThat(service.findBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xac, (byte)0x10, (byte)0x00, (byte)0x00 }, 12))).isNull();
	}
	
	@Test
	public void findUnknownByKey() {
		assertThat(service.findBlockedNetwork(new BlockedNetworkKey())).isNull();
	}

	@Test
	public void addNewNetwork() {
		BlockedNetworkRecord record = service.addBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xac, (byte)0x10, (byte)0x00, (byte)0x00 }, 12));
		
		assertThat(record).isNotNull();
		assertThat(record.getRange()).isEqualTo(new CIDR4Address(new byte[] { (byte)0xac, (byte)0x10, (byte)0x00, (byte)0x00 }, 12));
	}

	@Test(expected=RecordAlreadyExistsException.class)
	public void addKnownNetwork() {
		service.addBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16));
	}

	@Test
	public void deleteKnownNetwork() {
		service.deleteBlockedNetwork(key_192_168_0_0_16);
		
		assertThat(repository.findOne(key_192_168_0_0_16)).isNull();
	}
	
	@Test(expected=RecordNotFoundException.class)
	public void deleteUnknownRecord() {
		service.deleteBlockedNetwork(new BlockedNetworkKey());
	}
	
	@Test
	public void hostInRange() {
		assertThat(service.findContainingBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x03, (byte)0x10 }, 32)))
			.isEqualTo(new BlockedNetworkRecord(key_192_168_0_0_16, 
					new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16)));		
	}

	@Test
	public void hostNotInRange() {
		assertThat(service.findContainingBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xac, (byte)0x10, (byte)0x03, (byte)0x10 }, 32))).isNull();
	}

	@Test
	public void notHostInRange() {
		assertThat(service.findContainingBlockedNetwork(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x03, (byte)0x10 }, 30))).isNull();
	}
}
