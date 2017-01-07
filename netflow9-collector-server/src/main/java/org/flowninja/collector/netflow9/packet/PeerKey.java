package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * key for addressing peer in peer registry using peer Inet address and source
 * ID
 *
 * @author rainer
 *
 */
@EqualsAndHashCode
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PeerKey {
	private InetAddress peerAddress;
	private long sourceID;
}