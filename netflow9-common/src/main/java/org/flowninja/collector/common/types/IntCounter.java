/**
 *
 */
package org.flowninja.collector.common.types;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@EqualsAndHashCode(callSuper=false)
@ToString
public class IntCounter extends PrimitveCounter<Integer> {

    private final int value;

    IntCounter(byte[] binaryRepresentation) {
        this.value = (binaryRepresentation[0] & 0x00ff) << 24
                | (binaryRepresentation[1] & 0x00ff) << 16
                | (binaryRepresentation[2] & 0x00ff) << 8
                | binaryRepresentation[3] & 0x00ff;
    }

    @Override
    public String printableValue() {
        return Integer.toString(this.value);
    }

    @Override
    public Number value() {
        return value;
    }

}
