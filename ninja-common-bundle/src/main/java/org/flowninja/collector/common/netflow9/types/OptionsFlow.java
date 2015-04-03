/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author rainer
 *
 */
public class OptionsFlow {

	private InetAddress peerAddress;
	private Header header;
	private List<ScopeFlowRecord> scopes = new LinkedList<ScopeFlowRecord>();
	private List<OptionsFlowRecord> records = new LinkedList<OptionsFlowRecord>();
	
	public OptionsFlow(InetAddress peerAddress, Header header, List<ScopeFlowRecord> scopes, List<OptionsFlowRecord> records) {
		this.peerAddress = peerAddress;
		this.header = header;
		
		if(scopes != null)
			this.scopes.addAll(scopes);
		
		if(records != null) 
			this.records.addAll(records);
	}

	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * @return the records
	 */
	public List<OptionsFlowRecord> getRecords() {
		return Collections.unmodifiableList(records);
	}

	/**
	 * @return the scopes
	 */
	public List<ScopeFlowRecord> getScopes() {
		return scopes;
	}

	/**
	 * @return the peerAddress
	 */
	public InetAddress getPeerAddress() {
		return peerAddress;
	}

}
