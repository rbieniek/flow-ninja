/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

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
}
