/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rainer
 *
 */
@Getter
@Setter
public class PortableFlowValueRecord {

    private String type;
    private String stringValue;
    private String addrValue;
    private BigInteger numberValue;
    private String encodedValue;
    private Enum<?> enumValue;
}
