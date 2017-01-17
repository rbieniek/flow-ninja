/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PortableOptionsFlow {

    private String peerAddress;
    private Header header;
    private String uuid;
    private List<PortableScopeFlowRecord> scopes;
    private List<PortableFlowValueRecord> records;
}
