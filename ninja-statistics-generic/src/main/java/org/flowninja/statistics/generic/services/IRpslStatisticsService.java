/**
 * 
 */
package org.flowninja.statistics.generic.services;

import java.util.SortedSet;

/**
 * Interface implemented by usage recording service for RPSL Whois service
 * 
 * @author rainer
 *
 */
public interface IRpslStatisticsService {
	/**
	 * 
	 */
	void recordLookupRequest();

	/**
	 * 
	 */
	void recordAdminsitrativeBlocked();

	/**
	 * 
	 */
	void recordResultFromCache();
	
	/**
	 * 
	 */
	void recordResultFromRdapService();

	/**
	 * 
	 */
	void recordResultFromJsonService();

	/**
	 * 
	 */
	void recordResultFromWhoisService();
	
	/**
	 * 
	 */
	void recordNotFound();
	
	/**
	 * 
	 */
	void recordBadRequest();
	
	/**
	 * get the history of recorded statistics data
	 * 
	 * @return
	 */
	SortedSet<RpslStatisticsData> history();
}
