/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class NetworkFlowCollection implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7591401731013361923L;
	
	@JsonProperty(value="flows", required=true)
	private List<NetworkFlow> flows = new LinkedList<NetworkFlow>();

	public NetworkFlowCollection() {}
	
	public NetworkFlowCollection(Collection<NetworkFlow> flows) {
		this.flows.addAll(flows);
	}
	
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
