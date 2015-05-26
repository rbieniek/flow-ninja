/**
 * 
 */
package org.flowninja.statistics.redis.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.flowninja.statistics.generic.services.IRpslStatisticsService;
import org.flowninja.statistics.generic.services.RpslStatisticsData;
import org.flowninja.statistics.generic.services.RpslStatisticsDataComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author rainer
 *
 */
public class RedisRpslStatisticsService implements IRpslStatisticsService {
	private static final int HISTORY_DAYS = 30;

	private static final String KEY_ADMINSTRATIVELY_BLOCKED = "adminstrativelyBlocked";

	private static final String KEY_BAD_REQUEST = "badRequest";

	private static final String KEY_NOT_FOUND = "notFound";

	private static final String KEY_RESULTS_FROM_WHOIS_SERVICE = "resultsFromWhoisService";

	private static final String KEY_RESULTS_FROM_JSON_SERVICE = "resultsFromJsonService";

	private static final String KEY_RESULTS_FROM_CACHE = "resultsFromCache";

	private static final String KEY_LOOKUP_REQUESTS = "lookupRequests";

	private static final Logger logger = LoggerFactory.getLogger(RedisRpslStatisticsService.class);

	RedisTemplate<LocalDateTime, Map<String, Long>> redisTemplate;
	private int historyDays = HISTORY_DAYS;
	
	@Override
	public void recordLookupRequest() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_LOOKUP_REQUESTS, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordResultFromCache() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_RESULTS_FROM_CACHE, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordResultFromJsonService() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_RESULTS_FROM_JSON_SERVICE, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordResultFromWhoisService() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_RESULTS_FROM_WHOIS_SERVICE, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordNotFound() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
		
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_NOT_FOUND, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordBadRequest() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_BAD_REQUEST, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	@Override
	public void recordAdminsitrativeBlocked() {
		try {
			BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(LocalDateTime.now());
			
			ops.expire(historyDays, TimeUnit.DAYS);			
			ops.increment(KEY_ADMINSTRATIVELY_BLOCKED, 1);
		} catch(DataAccessException e) {
			logger.error("failed to record lookup request", e);
		}
	}

	public void setTemplate(RedisTemplate<LocalDateTime, Map<String, Long>> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public SortedSet<RpslStatisticsData> history() {
		SortedSet<RpslStatisticsData> set = new TreeSet<RpslStatisticsData>(new RpslStatisticsDataComparator());
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

		for(int i=0; i<historyDays*24; i++) {
			LocalDateTime stamp = now.minusHours(i);
			
			if(redisTemplate.hasKey(stamp)) {
				BoundHashOperations<LocalDateTime, String, Long> ops = redisTemplate.boundHashOps(stamp);
				Map<String, Long> map = ops.entries();
				
				set.add(new RpslStatisticsData(stamp, 
						defGet(map, KEY_LOOKUP_REQUESTS), 
						defGet(map, KEY_BAD_REQUEST),
						defGet(map, KEY_NOT_FOUND),
						defGet(map, KEY_ADMINSTRATIVELY_BLOCKED),
						defGet(map, KEY_RESULTS_FROM_CACHE),
						defGet(map, KEY_RESULTS_FROM_JSON_SERVICE),
						defGet(map, KEY_RESULTS_FROM_WHOIS_SERVICE)));
			} else 
				set.add(new RpslStatisticsData(stamp));
		}
		
		return set;
	}

	private Long defGet(Map<String, Long> map, String key) {
		if(map.containsKey(key))
			return map.get(key);
		else
			return 0L;
	}
	
	/**
	 * @param historyDays the historyDays to set
	 */
	public void setHistoryDays(int historyDays) {
		this.historyDays = historyDays;
	}
	
}
