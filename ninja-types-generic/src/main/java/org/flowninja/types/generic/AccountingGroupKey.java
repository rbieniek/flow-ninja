/**
 * 
 */
package org.flowninja.types.generic;

import java.util.UUID;

/**
 * @author rainer
 *
 */
public class AccountingGroupKey extends KeyBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471743562836982489L;

	/**
	 * 
	 */
	public AccountingGroupKey() {
		super(UUID.randomUUID());
	}

	/**
	 * @param uuid
	 */
	public AccountingGroupKey(UUID uuid) {
		super(uuid);
	}
}
