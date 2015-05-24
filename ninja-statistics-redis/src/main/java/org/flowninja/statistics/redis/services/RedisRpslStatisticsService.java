/**
 * 
 */
package org.flowninja.statistics.redis.services;

import org.flowninja.statistics.generic.services.IRpslStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class RedisRpslStatisticsService implements IRpslStatisticsService {
	private static final Logger logger = LoggerFactory.getLogger(RedisRpslStatisticsService.class);

	@Override
	public void recordLookupRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordResultFromCache() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordResultFromJsonService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordResultFromWhoisService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordNotFound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordBadRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordAdminsitrativeBlocked() {
		// TODO Auto-generated method stub
		
	}
	
}
