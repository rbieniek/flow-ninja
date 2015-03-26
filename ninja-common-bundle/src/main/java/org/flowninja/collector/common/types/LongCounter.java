/**
 * 
 */
package org.flowninja.collector.common.types;

/**
 * @author rainer
 *
 */
public class LongCounter extends PrimitveCounter<Long> {

	private long value;
	
	LongCounter(byte[] binaryRepresentation) {
		this.value = ((((long)binaryRepresentation[0]) & 0x00ff) << 56) 
				| ((((long)binaryRepresentation[1]) & 0x00ff) << 48) 
				| ((((long)binaryRepresentation[2]) & 0x00ff) << 40) 
				| ((((long)binaryRepresentation[3]) & 0x00ff) << 32)
				| ((((long)binaryRepresentation[4]) & 0x00ff) << 24) 
				| ((((long)binaryRepresentation[5]) & 0x00ff) << 16) 
				| ((((long)binaryRepresentation[6]) & 0x00ff) << 8) 
				| (((long)binaryRepresentation[7]) & 0x00ff);
	}

	@Override
	public String printableValue() {
		return Long.toString(value);
	}

	@Override
	public Number value() {
		return value;
	}

}
