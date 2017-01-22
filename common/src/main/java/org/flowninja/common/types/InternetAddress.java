package org.flowninja.common.types;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class InternetAddress {

    private String base64Rep;
    private String textRep;
    private InternetAddressType type;
    private Integer port;
    private Integer mask;
    private Integer asNumber;
    private Integer vlanNumber;
    private BigInteger snmpInterfaceIndex;
    private Integer trafficIndex;
}
