/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;
import java.net.SocketAddress;

import org.flowninja.collector.netflow9.components.PeerAddressMapper;

/**
 * @author rainer
 *
 */
public class StaticAddressPeerAddressMapper implements PeerAddressMapper {

	/* (non-Javadoc)
	 * @see org.flowninja.collector.netflow9.packet.PeerAddressMapper#mapRemoteAddress(java.net.SocketAddress)
	 */
	@Override
	public InetAddress mapRemoteAddress(SocketAddress address) throws Exception {
		return InetAddress.getByName("127.0.0.1");
	}

}
