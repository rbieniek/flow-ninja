/**
 *
 */
package org.flowninja.common.types;

import java.util.LinkedList;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PortableDataFlow {

    private Header header;
    private String peerAddress;
    private List<PortableFlowValueRecord> records = new LinkedList<>();
    private String uuid;

    private InternetAddress srcAddress;
    private InternetAddress dstAddress;
    private InternetAddress nextHop;
    private InternetAddress bgpNextHop;
    private PayloadCounters payloadCounters;
}
