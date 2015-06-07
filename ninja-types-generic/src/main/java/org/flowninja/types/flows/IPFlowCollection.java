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
	private List<IPv4Flow> flows = new LinkedList<IPv4Flow>();

	/**
	 * @return the flows
	 */
	public List<IPv4Flow> getFlows() {
		return flows;
	}

	/**
	 * @param flows the flows to set
	 */
	public void setFlows(List<IPv4Flow> flows) {
		this.flows = flows;
	}
	
}
