/**
 * 
 */
package org.flowninja.collector.common.netflow9.services;

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
	 * @param msg
	 */
	void storeDataFlow(DataFlow msg);

	/**
	 * Store an options flow
	 * 
	 * @param msg
	 */
	void storeOptionsFlow(OptionsFlow msg);

}
