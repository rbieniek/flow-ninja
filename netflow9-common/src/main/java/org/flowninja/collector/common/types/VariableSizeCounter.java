/**
 *
 */
package org.flowninja.collector.common.types;

import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@EqualsAndHashCode
@ToString
public class VariableSizeCounter implements Counter {

    private final BigInteger value;

    VariableSizeCounter(final byte[] binaryRepresentation) {
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
