/**
 * 
 */
package org.flowninja.collector.netflow9.components;

import java.net.SocketAddress;

import java.net.InetAddress;

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
