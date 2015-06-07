/**
 * 
 */
package org.flowninja.webapp.collector.generic.restcontrollers;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class IPv4FlowInfoModel {
	@JsonProperty
	private long totalRecords;
	
	public IPv4FlowInfoModel(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the totalRecords
	 */
	public long getTotalRecords() {
		return totalRecords;
	}
}
