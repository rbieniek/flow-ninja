/**
 * 
 */
package org.flowninja.collector.common.netflow9.services;

import java.net.InetAddress;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;

/**
 * @author rainer
 *
 */
public interface FlowStoreService {
	/**
	 * activate a flow storer
	 */
	void activateFlowStorer();

	/**
	 * deactivate a flow storer
	 */
	void passivateFlowStorer();

	/**
	 * Store a data flow
	 * 
	 * @param peerAddress
	 * @param msg
	 */
	void storeDataFlow(InetAddress peerAddress, DataFlow msg);

	/**
	 * Store an options flow
	 * 
	 * @param peerAddress
	 * @param msg
	 */
	void storeOptionsFlow(InetAddress peerAddress, OptionsFlow msg);

}
