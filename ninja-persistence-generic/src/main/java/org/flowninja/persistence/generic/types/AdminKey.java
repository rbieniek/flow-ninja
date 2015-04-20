/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class AdminKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 690987643369915002L; 

	public AdminKey() {
		super(UUID.randomUUID());
	}
	
	public AdminKey(UUID uuid) {
		super(uuid);
	}
	
}
