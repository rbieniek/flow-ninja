/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.flowninja.types.net.CIDR4Address;

/**
 * @author rainer
 *
 */
public class BlockedNetworkRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7058700742689250210L;

	private BlockedNetworkKey key;
	private CIDR4Address range;
	
	public BlockedNetworkRecord() {}
	
	public BlockedNetworkRecord(BlockedNetworkKey key, CIDR4Address range) {
		this.key = key;
		this.range = range;
	}

	/**
	 * @return the key
	 */
	public BlockedNetworkKey getKey() {
		return key;
	}

	/**
	 * @return the range
	 */
	public CIDR4Address getRange() {
		return range;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BlockedNetworkRecord))
			return false;
		
		BlockedNetworkRecord o = (BlockedNetworkRecord)obj;
		
		return (new EqualsBuilder())
				.append(this.key, o.key)
				.append(this.range, o.range)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.key)
				.append(this.range)
				.toHashCode();
	}
	
}
