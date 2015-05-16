/**
 * 
 */
package org.flowninja.types.generic;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class BlockedNetworkKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 690987643369915002L; 

	public BlockedNetworkKey() {
		super(UUID.randomUUID());
	}
	
	public BlockedNetworkKey(UUID uuid) {
		super(uuid);
	}
	
}
