/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.math.BigInteger;
import java.util.List;

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
    private String objectClass;
    private String stringValue;
    private String addrValue;
    private BigInteger numberValue;
    private String encodedValue;
    private String enumValue;
    private List<PortableFlowValueRecord> collectionValue;
}
