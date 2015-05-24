/**
 * 
 */
package org.flowninja.statistics.generic.services;

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
}
