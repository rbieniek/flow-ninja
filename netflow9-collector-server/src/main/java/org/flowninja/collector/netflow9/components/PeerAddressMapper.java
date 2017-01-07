/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.net.InetAddress;
import java.net.SocketAddress;

/**
 * @author rainer
 *
 */
public interface PeerAddressMapper {
	/**
	 *
	 * @param address
	 * @return
	 */
	public InetAddress mapRemoteAddress(SocketAddress address) throws Exception;
}
