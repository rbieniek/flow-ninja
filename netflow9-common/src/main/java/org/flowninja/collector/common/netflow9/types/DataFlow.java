/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.net.InetAddress;
import java.util.LinkedList;
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
public class DataFlow {

	private Header header;
	private InetAddress peerAddress;
	private List<FlowValueRecord> records = new LinkedList<>();
	private UUID uuid = UUID.randomUUID();
}
