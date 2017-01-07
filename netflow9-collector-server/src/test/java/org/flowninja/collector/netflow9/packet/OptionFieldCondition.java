/**
 *
 */
package org.flowninja.collector.netflow9.packet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fest.assertions.core.Condition;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.OptionField;

/**
 * @author rainer
 *
 */
public class OptionFieldCondition extends Condition<OptionField> {

	private FieldType type;
	private int length;

	public OptionFieldCondition(final FieldType type, final int length) {
		this.length = length;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fest.assertions.core.Condition#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(final OptionField value) {
		return value.getLength() == length && value.getType() == type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
