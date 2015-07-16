/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
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
	
	@JsonProperty(value="first", required=true)
	private Date firstStamp;
	
	@JsonProperty(value="last", required=true)
	@JsonFormat(shape=Shape.STRING)
	private Date lastStamp;

	@JsonProperty(value="flows", required=true)
	@JsonFormat(shape=Shape.STRING)
	private List<NetworkFlow> flows = new LinkedList<NetworkFlow>();


	public NetworkFlowCollection() {}
	
	public NetworkFlowCollection(Date fistStamp, Date lastStamp, Collection<NetworkFlow> flows) {
		this.firstStamp = fistStamp;
		this.lastStamp = lastStamp;
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

	/**
	 * @return the firstStamp
	 */
	public Date getFirstStamp() {
		return firstStamp;
	}

	/**
	 * @param firstStamp the firstStamp to set
	 */
	public void setFirstStamp(Date firstStamp) {
		this.firstStamp = firstStamp;
	}

	/**
	 * @return the lastStamp
	 */
	public Date getLastStamp() {
		return lastStamp;
	}

	/**
	 * @param lastStamp the lastStamp to set
	 */
	public void setLastStamp(Date lastStamp) {
		this.lastStamp = lastStamp;
	}
}
