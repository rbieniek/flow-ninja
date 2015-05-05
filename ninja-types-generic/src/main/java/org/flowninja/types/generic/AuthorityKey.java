/**
 * 
 */
package org.flowninja.types.generic;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class AuthorityKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 690987643369915002L; 

	public AuthorityKey() {
		super(UUID.randomUUID());
	}
	
	public AuthorityKey(UUID uuid) {
		super(uuid);
	}
	
}
