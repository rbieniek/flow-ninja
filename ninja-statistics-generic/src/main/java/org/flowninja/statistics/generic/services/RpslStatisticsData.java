/**
 * 
 */
package org.flowninja.statistics.generic.services;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class RpslStatisticsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4531648775936766968L;

	private LocalDateTime stamp;
	private long lookupRequests;
	private long badRequests;
	private long notFound;
	private long administrativelyBlocked;
	private long resultsFromCache;
	private long resultsFromJsonService;
	private long resultsFromWhoisService;
	private long resultsFromRdapService;
	
	public RpslStatisticsData() {}

	public RpslStatisticsData(LocalDateTime stamp) {
		this.stamp = stamp;
	}

	public RpslStatisticsData(LocalDateTime stamp, long lookupRequests, long badRequests, long notFound, long administrativelyBlocked, 
			long resultsFromCache, long resultsFromJsonService, long resultsFromWhoisService, long resultsFromRdapService) {
		this.stamp = stamp;
		this.lookupRequests = lookupRequests;
		this.badRequests = badRequests;
		this.notFound = notFound;
		this.administrativelyBlocked = administrativelyBlocked;
		this.resultsFromCache = resultsFromCache;
		this.resultsFromJsonService = resultsFromJsonService;
		this.resultsFromWhoisService = resultsFromWhoisService;
		this.resultsFromRdapService = resultsFromRdapService;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the stamp
	 */
	public LocalDateTime getStamp() {
		return stamp;
	}

	/**
	 * @return the lookupRequests
	 */
	public long getLookupRequests() {
		return lookupRequests;
	}

	/**
	 * @return the badRequests
	 */
	public long getBadRequests() {
		return badRequests;
	}

	/**
	 * @return the notFound
	 */
	public long getNotFound() {
		return notFound;
	}

	/**
	 * @return the administrativelyBlocked
	 */
	public long getAdministrativelyBlocked() {
		return administrativelyBlocked;
	}

	/**
	 * @return the resultsFromCache
	 */
	public long getResultsFromCache() {
		return resultsFromCache;
	}

	/**
	 * @return the resultsFromJsonService
	 */
	public long getResultsFromJsonService() {
		return resultsFromJsonService;
	}

	/**
	 * @return the resultsFromWhoisService
	 */
	public long getResultsFromWhoisService() {
		return resultsFromWhoisService;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RpslStatisticsData))
			return false;
		
		RpslStatisticsData o = (RpslStatisticsData)obj;
		
		return (new EqualsBuilder())
				.append(this.stamp, o.stamp)
				.append(this.lookupRequests, o.lookupRequests)
				.append(this.badRequests, o.badRequests)
				.append(this.notFound, o.notFound)
				.append(this.administrativelyBlocked, o.administrativelyBlocked)
				.append(this.resultsFromCache, o.resultsFromCache)
				.append(this.resultsFromJsonService, o.resultsFromJsonService)
				.append(this.resultsFromWhoisService, o.resultsFromWhoisService)
				.append(this.resultsFromRdapService, o.resultsFromRdapService)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(stamp)
				.append(lookupRequests)
				.append(badRequests)
				.append(notFound)
				.append(administrativelyBlocked)
				.append(resultsFromCache)
				.append(resultsFromJsonService)
				.append(resultsFromWhoisService)
				.append(resultsFromRdapService)
				.toHashCode();
	}

	/**
	 * @param resultsFromRdapService the resultsFromRdapService to set
	 */
	public void setResultsFromRdapService(long resultsFromRdapService) {
		this.resultsFromRdapService = resultsFromRdapService;
	}
	
}
