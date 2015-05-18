/**
 * 
 */
package org.flowninja.persistence.rspl.redis.services;

import java.util.concurrent.TimeUnit;

import org.flowninja.persistence.rspl.generic.services.INetworkResourceLoadService;
import org.flowninja.persistence.rspl.generic.services.INetworkResourcePersistService;
import org.flowninja.persistence.rspl.generic.types.NetworkInformation;
import org.flowninja.persistence.rspl.redis.components.CIDR4AddressRedisSerializer;
import org.flowninja.types.net.CIDR4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author rainer
 *
 */
public class RedisRsplPersistenceService implements	INetworkResourcePersistService, INetworkResourceLoadService {
	private static final Logger logger = LoggerFactory.getLogger(RedisRsplPersistenceService.class);

	private RedisTemplate<CIDR4Address, NetworkInformation> masterTemplate;
	private RedisTemplate<CIDR4Address, NetworkInformation> slaveTemplate;
	private long retainTime = 8;
	private TimeUnit retainUnit = TimeUnit.DAYS;
	
	/* (non-Javadoc)
	 * @see org.flowninja.persistence.rspl.generic.services.INetworkResourceLoadService#loadNetworkInformation(org.flowninja.types.net.CIDR4Address)
	 */
	@Override
	public NetworkInformation loadNetworkInformation(CIDR4Address address) {
		NetworkInformation networkInformation = null;

		logger.info("loading key {}", address);

		
		boolean exists = slaveTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					return connection.exists(((CIDR4AddressRedisSerializer)slaveTemplate.getKeySerializer()).serialize(address));					
				} catch(RedisConnectionFailureException e) {
					logger.warn("failed to retrieve key {}", address, e);
					
					return false;
				}
			}
		});
		
		if(exists)
			networkInformation = slaveTemplate.opsForValue().get(address);			
		

		logger.info("loading from key {} yielded {}", address, networkInformation);

		return networkInformation;
	}

	/* (non-Javadoc)
	 * @see org.flowninja.persistence.rspl.generic.services.INetworkResourcePersistService#persistNetworkInformation(org.flowninja.types.net.CIDR4Address, org.flowninja.persistence.rspl.generic.types.NetworkInformation)
	 */
	@Override
	public void persistNetworkInformation(CIDR4Address address, NetworkInformation networkInfo) {
		logger.info("persisting address {} with network info {}", address, networkInfo);
		
		masterTemplate.opsForValue().set(address, networkInfo, retainTime, retainUnit);
	}

	/**
	 * @return the masterTemplate
	 */
	public RedisTemplate<CIDR4Address, NetworkInformation> getMasterTemplate() {
		return masterTemplate;
	}

	/**
	 * @param masterTemplate the masterTemplate to set
	 */
	public void setMasterTemplate(RedisTemplate<CIDR4Address, NetworkInformation> masterTemplate) {
		this.masterTemplate = masterTemplate;
	}

	/**
	 * @return the slaveTemplate
	 */
	public RedisTemplate<CIDR4Address, NetworkInformation> getSlaveTemplate() {
		return slaveTemplate;
	}

	/**
	 * @param slaveTemplate the slaveTemplate to set
	 */
	public void setSlaveTemplate(
			RedisTemplate<CIDR4Address, NetworkInformation> slaveTemplate) {
		this.slaveTemplate = slaveTemplate;
	}

	/**
	 * @return the retainTime
	 */
	public long getRetainTime() {
		return retainTime;
	}

	/**
	 * @param retainTime the retainTime to set
	 */
	public void setRetainTime(long retainTime) {
		this.retainTime = retainTime;
	}

	/**
	 * @return the retainUnit
	 */
	public TimeUnit getRetainUnit() {
		return retainUnit;
	}

	/**
	 * @param retainUnit the retainUnit to set
	 */
	public void setRetainUnit(TimeUnit retainUnit) {
		this.retainUnit = retainUnit;
	}

}
