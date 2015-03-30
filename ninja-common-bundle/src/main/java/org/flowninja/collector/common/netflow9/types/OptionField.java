/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Option field transported in Options template
 * 
 * @author rainer
 *
 */
public class OptionField {
	private FieldType type;
	private int length;
	
	public OptionField(FieldType type, int length) {
		this.type = type;
		this.length = length;
	}

	/**
	 * @return the type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
