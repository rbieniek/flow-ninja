/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class NetworkFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5352414752965824253L;

	public static class Builder {
		NetworkFlow flow = new NetworkFlow();
		
		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public Builder withPeerIp(String peerIp) {
			flow.setPeerIp(peerIp);
			
			return this;
		}
		
		public Builder withFlowUuid(String uuid) {
			flow.setFlowUuid(uuid);
			
			return this;
		}
		
		public Builder withHeader(FlowHeader header) {
			flow.setHeader(header);
			
			return this;
		}
		
		public Builder withData(FlowData data) {
			flow.setData(data);
			
			return this;
		}
		
		public NetworkFlow build() {
			return flow;
		}
	}
	
	@JsonProperty(value="peerIp", required=true)
	private String peerIp;

	@JsonProperty(value="uuid", required=true)
	private String flowUuid;

	@JsonProperty(value="header", required=true)
	private FlowHeader header;
	
	@JsonProperty(value="data", required=true)
	private FlowData data;
	
	@JsonIgnore
	private String accountingGroupUuid;
	
	@JsonIgnore
	private String collectorUuid;
	
	public NetworkFlow() {
		
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

	/**
	 * @return the data
	 */
	public FlowData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(FlowData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(accountingGroupUuid)
				.append(collectorUuid)
				.append(data)
				.append(flowUuid)
				.append(header)
				.append(peerIp)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NetworkFlow))
			return false;
		
		NetworkFlow o = (NetworkFlow)obj;
		
		return (new EqualsBuilder())
				.append(this.accountingGroupUuid, o.accountingGroupUuid)
				.append(this.collectorUuid, o.collectorUuid)
				.append(this.data, o.data)
				.append(this.flowUuid, o.flowUuid)
				.append(this.header, o.header)
				.append(this.peerIp, o.peerIp)
				.isEquals();
	}
}
