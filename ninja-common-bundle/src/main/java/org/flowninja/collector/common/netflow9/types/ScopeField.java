/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

/**
 * Scope field transported in an Options template
 * 
 * @author rainer
 *
 */
public class ScopeField {
	private int length;
	private ScopeType type;
	
	public ScopeField(ScopeType type, int length) {
		this.length = length;
		this.type = type;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the type
	 */
	public ScopeType getType() {
		return type;
	}
}
