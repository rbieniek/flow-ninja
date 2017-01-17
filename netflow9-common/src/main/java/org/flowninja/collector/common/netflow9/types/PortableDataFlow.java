/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rainer
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortableDataFlow {

    private Header header;
    private String peerAddress;
    private List<PortableFlowValueRecord> records = new LinkedList<>();
    private String uuid;
}
