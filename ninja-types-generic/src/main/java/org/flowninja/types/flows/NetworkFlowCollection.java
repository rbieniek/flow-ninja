/**
 * 
 */
package org.flowninja.types.flows;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class NetworkFlowCollection {
	@JsonProperty(value="flows", required=true)
	private List<NetworkFlow> flows = new LinkedList<NetworkFlow>();

	/**
	 * @return the flows
	 */
	public List<NetworkFlow> getFlows() {
		return flows;
	}

	/**
	 * @param flows the flows to set
	 */
	public void setFlows(List<NetworkFlow> flows) {
		this.flows = flows;
	}
	
}
