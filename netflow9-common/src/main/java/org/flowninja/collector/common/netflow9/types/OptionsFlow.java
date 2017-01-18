/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import org.flowninja.common.types.Header;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class OptionsFlow {

    private InetAddress peerAddress;
    private Header header;
    private UUID uuid;
    private List<ScopeFlowRecord> scopes;
    private List<FlowValueRecord> records;
}
