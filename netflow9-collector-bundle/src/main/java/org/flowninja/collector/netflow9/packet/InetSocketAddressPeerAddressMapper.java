/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author rainer
 *
 */
public class InetSocketAddressPeerAddressMapper implements PeerAddressMapper {

	/* (non-Javadoc)
	 * @see org.flowninja.collector.netflow9.packet.PeerAddressMapper#mapRemoteAddress(java.net.SocketAddress)
	 */
	@Override
	public InetAddress mapRemoteAddress(SocketAddress address) {
		return ((InetSocketAddress)address).getAddress();
	}

}
