/**
 *
 */
package org.flowninja.common.types;

import java.math.BigInteger;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
