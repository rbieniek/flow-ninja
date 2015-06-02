/**
 * 
 */
package org.flowninja.types.generic;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class CollectorKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471743562836982489L;

	/**
	 * 
	 */
	public CollectorKey() {
		super(UUID.randomUUID());
	}

	/**
	 * @param uuid
	 */
	public CollectorKey(UUID uuid) {
		super(uuid);
	}
}
