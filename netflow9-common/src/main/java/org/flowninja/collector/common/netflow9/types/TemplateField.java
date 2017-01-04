/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class TemplateField {
	private FieldType type;
	private int length;
	
	public TemplateField(FieldType type, int length) {
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
