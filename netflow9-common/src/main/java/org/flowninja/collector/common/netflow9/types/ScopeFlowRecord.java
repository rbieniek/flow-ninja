/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import org.flowninja.collector.common.types.Counter;

/**
 * @author rainer
 *
 */
public class ScopeFlowRecord {
	ScopeType type;
	private Counter value;
	
	public ScopeFlowRecord(ScopeType type, Counter value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public ScopeType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public Counter getValue() {
		return value;
	}
}
