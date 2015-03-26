/**
 * 
 */
package org.flowninja.collector.common.types;

import java.math.BigInteger;

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

}
