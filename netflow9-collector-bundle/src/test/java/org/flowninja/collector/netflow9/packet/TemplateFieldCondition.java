/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fest.assertions.core.Condition;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.TemplateField;

/**
 * @author rainer
 *
 */
public class TemplateFieldCondition extends Condition<TemplateField> {

	private FieldType type;
	private int length;

	public TemplateFieldCondition(FieldType type, int length) {
		this.length = length;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see org.fest.assertions.core.Condition#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(TemplateField value) {
		return value.getLength() == length && value.getType() == type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
