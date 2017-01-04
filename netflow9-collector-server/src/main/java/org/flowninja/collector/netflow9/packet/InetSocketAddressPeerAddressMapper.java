/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class InetSocketAddressPeerAddressMapper implements PeerAddressMapper {
	private static final Logger logger = LoggerFactory.getLogger(InetSocketAddressPeerAddressMapper.class);

	private InetAddress nullAddress;

	public InetSocketAddressPeerAddressMapper() {
		try {
			nullAddress = InetAddress.getByAddress(new byte[] { 0x00, 0x00, 0x00, 0x00 });
		} catch(UnknownHostException e) {
			logger.error("failed to create default address", e);
			
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.flowninja.collector.netflow9.packet.PeerAddressMapper#mapRemoteAddress(java.net.SocketAddress)
	 */
	@Override
	public InetAddress mapRemoteAddress(SocketAddress address) {
		logger.info("Received packet from socket address '{}', class='{}'", 
				address, (address != null ? address.getClass().getName() : "<unknown>"));
		
		if(address != null)
			return ((InetSocketAddress)address).getAddress();
		else 
			return nullAddress;
	}

}
