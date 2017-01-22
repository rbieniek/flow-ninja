/**
 *
 */
package org.flowninja.common.types;

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

    private InternetAddress srcAddress;
    private InternetAddress dstAddress;
    private InternetAddress nextHop;
    private InternetAddress bgpNextHop;
    private ProtocolFlags protocolFlags;
    private PayloadCounters payloadCounters;
    private TypeOfService typeOfService;
    private FlowStatistics flowStatistics;

}
