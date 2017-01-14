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
public class StaticAddressPeerAddressMapper implements PeerAddressMapper {

    /*
     * (non-Javadoc)
     *
     * @see org.flowninja.collector.netflow9.packet.PeerAddressMapper# mapRemoteAddress(java.net.SocketAddress)
     */
    @Override
    public InetAddress mapRemoteAddress(final SocketAddress address) throws Exception {
        return InetAddress.getLoopbackAddress();
    }

}
