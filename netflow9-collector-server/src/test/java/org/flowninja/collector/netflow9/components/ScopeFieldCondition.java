/**
 *
 */
package org.flowninja.collector.netflow9.components;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fest.assertions.core.Condition;
import org.flowninja.collector.common.netflow9.types.ScopeField;
import org.flowninja.common.types.ScopeType;

/**
 * @author rainer
 *
 */
public class ScopeFieldCondition extends Condition<ScopeField> {

	private ScopeType type;
	private int length;

	public ScopeFieldCondition(final ScopeType type, final int length) {
		this.length = length;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fest.assertions.core.Condition#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(final ScopeField value) {
		return value.getLength() == length && value.getType() == type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
