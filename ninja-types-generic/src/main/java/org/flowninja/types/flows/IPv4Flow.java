/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class IPv4Flow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5352414752965824253L;

	@JsonProperty(value="peerIp", required=true)
	private String peerIp;

	@JsonProperty(value="uuid", required=true)
	private String flowUuid;

	@JsonProperty(value="header", required=true)
	private FlowHeader header;
	
	@JsonIgnore
	private String accountingGroupUuid;
	
	@JsonIgnore
	private String collectorUuid;
	
	public IPv4Flow() {
		
	}

	/**
	 * @return the peerIp
	 */
	public String getPeerIp() {
		return peerIp;
	}

	/**
	 * @param peerIp the peerIp to set
	 */
	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	/**
	 * @return the flowUuid
	 */
	public String getFlowUuid() {
		return flowUuid;
	}

	/**
	 * @param flowUuid the flowUuid to set
	 */
	public void setFlowUuid(String flowUuid) {
		this.flowUuid = flowUuid;
	}

	/**
	 * @return the header
	 */
	public FlowHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(FlowHeader header) {
		this.header = header;
	}

	/**
	 * @return the accountingGroupUuid
	 */
	public String getAccountingGroupUuid() {
		return accountingGroupUuid;
	}

	/**
	 * @param accountingGroupUuid the accountingGroupUuid to set
	 */
	public void setAccountingGroupUuid(String accountingGroupUuid) {
		this.accountingGroupUuid = accountingGroupUuid;
	}

	/**
	 * @return the collectorUuid
	 */
	public String getCollectorUuid() {
		return collectorUuid;
	}

	/**
	 * @param collectorUuid the collectorUuid to set
	 */
	public void setCollectorUuid(String collectorUuid) {
		this.collectorUuid = collectorUuid;
	}
	
}
