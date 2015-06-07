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
public class IPFlowCollection {
	@JsonProperty(value="flows", required=true)
	private List<IPFlow> flows = new LinkedList<IPFlow>();

	/**
	 * @return the flows
	 */
	public List<IPFlow> getFlows() {
		return flows;
	}

	/**
	 * @param flows the flows to set
	 */
	public void setFlows(List<IPFlow> flows) {
		this.flows = flows;
	}
	
}
