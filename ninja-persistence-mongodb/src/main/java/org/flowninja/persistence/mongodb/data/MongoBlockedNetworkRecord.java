/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import org.flowninja.types.generic.BlockedNetworkKey;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author rainer
 *
 */
public class MongoBlockedNetworkRecord extends BaseRecord<BlockedNetworkKey> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6103685055453703722L;

	@Indexed
	private byte[] cidr;
	
	public MongoBlockedNetworkRecord() {}
	
	public MongoBlockedNetworkRecord(BlockedNetworkKey key, byte[] cidr) {
		super(key);
		
		this.cidr = cidr;
	}

	/**
	 * @return the cidr
	 */
	public byte[] getCidr() {
		return cidr;
	}

	/**
	 * @param cidr the cidr to set
	 */
	public void setCidr(byte[] cidr) {
		this.cidr = cidr;
	}
}
