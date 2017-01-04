/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

/**
 * @author rainer
 *
 */
public class DataFlowRecord {
	FieldType type;
	private Object value;
	
	public DataFlowRecord(FieldType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
}
