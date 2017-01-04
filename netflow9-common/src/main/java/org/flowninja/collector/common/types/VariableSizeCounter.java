/**
 * 
 */
package org.flowninja.collector.common.types;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @author rainer
 *
 */
public class VariableSizeCounter implements Counter {

	private BigInteger value;
	
	VariableSizeCounter(byte[] binaryRepresentation) {
		value = new BigInteger(binaryRepresentation);
	}

	@Override
	public String printableValue() {
		return value.toString();
	}

	@Override
	public Number value() {
		return value;
	}

	@Override
	public String toString() {
		return printableValue();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VariableSizeCounter))
			return false;
	
		VariableSizeCounter o = (VariableSizeCounter)obj;
	
		return (new EqualsBuilder()).append(this.value, o.value).isEquals();
	}
}
