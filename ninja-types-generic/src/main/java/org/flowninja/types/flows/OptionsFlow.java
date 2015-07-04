/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class OptionsFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8837026719677443375L;

	public static class Builder {
		private OptionsFlow flow = new OptionsFlow();
		private Set<FlowScope> scopes = new HashSet<FlowScope>();
		
		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public OptionsFlow build() {
			flow.setScopes(scopes);
			
			return flow;
		}
		
		public Builder withPeerIp(String addr) {
			flow.setPeerIp(addr);
			
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
		
		public Builder withData(OptionsData data) {
			flow.setData(data);
			
			return this;
		}
		
		public Builder withFlowScope(FlowScope scope) {
			scopes.add(scope);
			
			return this;
		}
	}
	
	@JsonProperty(value="peerIp", required=true)
	private String peerIp;

	@JsonProperty(value="uuid", required=true)
	private String flowUuid;

	@JsonProperty(value="header", required=true)
	private FlowHeader header;

	@JsonProperty(value="scopes")
	private Set<FlowScope> scopes;
	
	@JsonProperty(value="data", required=true)
	private OptionsData data;
	
	@JsonIgnore
	private String accountingGroupUuid;
	
	@JsonIgnore
	private String collectorUuid;

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
	 * @return the data
	 */
	public OptionsData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(OptionsData data) {
		this.data = data;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof OptionsFlow))
			return false;
		
		OptionsFlow o = (OptionsFlow)obj;
		
		return (new EqualsBuilder())
				.append(this.accountingGroupUuid, o.accountingGroupUuid)
				.append(this.collectorUuid, o.collectorUuid)
				.append(this.data, o.data)
				.append(this.flowUuid, o.flowUuid)
				.append(this.header, o.header)
				.append(this.peerIp, o.peerIp)
				.append(this.scopes, o.scopes)
				.isEquals();
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
				.append(scopes)
				.toHashCode();
	}

	/**
	 * @return the scopes
	 */
	public Set<FlowScope> getScopes() {
		return scopes;
	}

	/**
	 * @param scopes the scopes to set
	 */
	public void setScopes(Set<FlowScope> scopes) {
		this.scopes = scopes;
	}
}
