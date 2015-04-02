/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.net.InetAddress;

import org.flowninja.collector.common.netflow9.services.FlowStoreService;
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;

/**
 * @author rainer
 *
 */
public class FlatFileStorerService implements FlowStoreService {

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#activateFlowStorer()
	 */
	@Override
	public void activateFlowStorer() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#passivateFlowStorer()
	 */
	@Override
	public void passivateFlowStorer() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#storeDataFlow(java.net.InetAddress, org.flowninja.collector.common.netflow9.types.DataFlow)
	 */
	@Override
	public void storeDataFlow(InetAddress peerAddress, DataFlow msg) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#storeOptionsFlow(java.net.InetAddress, org.flowninja.collector.common.netflow9.types.OptionsFlow)
	 */
	@Override
	public void storeOptionsFlow(InetAddress peerAddress, OptionsFlow msg) {
		// TODO Auto-generated method stub

	}

}
