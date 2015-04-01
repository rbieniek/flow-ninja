/**
 * 
 */
package org.flowninja.collector.common.types;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @author rainer
 *
 */
public class IntCounter extends PrimitveCounter<Integer> {

	private int value;

	IntCounter(byte[] binaryRepresentation) {
		this.value = ((((int)binaryRepresentation[0]) & 0x00ff) << 24) 
				| ((((int)binaryRepresentation[1]) & 0x00ff) << 16) 
				| ((((int)binaryRepresentation[2]) & 0x00ff) << 8) 
				| (((int)binaryRepresentation[3]) & 0x00ff);
 	}

	@Override
	public String printableValue() {
		return Integer.toString(this.value);
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
		if(!(obj instanceof IntCounter))
			return false;
		
		IntCounter o = (IntCounter)obj;
		
		return (new EqualsBuilder()).append(this.value, o.value).isEquals();
	}

}
