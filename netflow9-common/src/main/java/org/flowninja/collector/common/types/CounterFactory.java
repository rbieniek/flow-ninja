/**
 * 
 */
package org.flowninja.collector.common.types;

/**
 * Factory for creating counter-like object instances
 * 
 * @author rainer
 *
 */
public class CounterFactory {

	/**
	 * Decode a counter-value representation in network-byte order into a counter object.
	 * For counter object with a representation of 32 or 64, it chooses an counter implementation based on Java primitive types.
	 * For all other lengths, an implementation handling an arbitrary size is chosen.
	 * 
	 * @param binaryRepresentation
	 * @return
	 */
	public static Counter decode(byte[] binaryRepresentation) {
		Counter counter = null;
		
		switch(binaryRepresentation.length) {
		case 4:
			counter = new IntCounter(binaryRepresentation);
			break;
		case 8:
			counter = new LongCounter(binaryRepresentation);
			break;
		default:
			counter = new VariableSizeCounter(binaryRepresentation);
		}
		return counter;
	}
}
