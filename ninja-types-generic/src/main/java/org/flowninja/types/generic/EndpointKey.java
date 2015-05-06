/**
 * 
 */
package org.flowninja.types.generic;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class EndpointKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5252173478932551289L;

	/**
	 * 
	 */
	public EndpointKey() {
	}

	/**
	 * @param uuid
	 */
	public EndpointKey(UUID uuid) {
		super(uuid);
	}

	/**
	 * @param uuid
	 */
	public EndpointKey(String uuid) {
		super(uuid);
	}

}
