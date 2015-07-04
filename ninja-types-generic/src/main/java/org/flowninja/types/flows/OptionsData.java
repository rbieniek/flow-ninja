/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class OptionsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4210502776384861620L;

	public static class Builder {
		private OptionsData data = new OptionsData();
		
		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public OptionsData build() {
			return data;
		}
		
		public Builder withTotalFlowsExported(BigInteger totalFlowsExported) {
			data.setTotalFlowsExported(totalFlowsExported);
			
			return this;
		}
		
		public Builder withTotalPacketsExported(BigInteger totalPacketsExported) {
			data.setTotalPacketsExported(totalPacketsExported);
			
			return this;
		}
	}
	
	@JsonProperty(value="totalFlowsExp")
	private BigInteger totalFlowsExported;
	
	@JsonProperty(value="totalPktsExp")
	private BigInteger totalPacketsExported;

	/**
	 * @return the totalFlowsExported
	 */
	public BigInteger getTotalFlowsExported() {
		return totalFlowsExported;
	}

	/**
	 * @param totalFlowsExported the totalFlowsExported to set
	 */
	public void setTotalFlowsExported(BigInteger totalFlowsExported) {
		this.totalFlowsExported = totalFlowsExported;
	}

	/**
	 * @return the totalPacketsExported
	 */
	public BigInteger getTotalPacketsExported() {
		return totalPacketsExported;
	}

	/**
	 * @param totalPacketsExported the totalPacketsExported to set
	 */
	public void setTotalPacketsExported(BigInteger totalPacketsExported) {
		this.totalPacketsExported = totalPacketsExported;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof OptionsData))
			return false;
		
		OptionsData o = (OptionsData)obj;
		
		return (new EqualsBuilder())
				.append(totalFlowsExported, o.totalFlowsExported)
				.append(totalPacketsExported, o.totalPacketsExported)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(totalFlowsExported)
				.append(totalPacketsExported)
				.toHashCode();
	}
}
