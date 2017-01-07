/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionsFlow {
	private InetAddress peerAddress;
	private Header header;
	private UUID uuid;
	private List<ScopeFlowRecord> scopes;
	private List<OptionsFlowRecord> records;
}
