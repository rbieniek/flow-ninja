package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * key for addressing peer in peer registry using peer Inet address and source ID
 * 
 * @author rainer
 *
 */
public class PeerKey {
	private InetAddress peerAddress;
	private long sourceID;
	
	public PeerKey(InetAddress peerAddress, long sourceID) {
		this.peerAddress = peerAddress;
		this.sourceID = sourceID;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PeerKey))
			return false;
		
		PeerKey o = (PeerKey)obj;
		
		return (new EqualsBuilder())
				.append(peerAddress, o.peerAddress)
				.append(sourceID, o.sourceID)
				.isEquals();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.peerAddress)
				.append(this.sourceID)
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}