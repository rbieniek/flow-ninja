/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * @author rainer
 *
 */
public class OptionsFlowCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5533883281986165999L;

	@JsonProperty(value="first", required=true)
	@JsonFormat(shape=Shape.STRING)
	private Date firstStamp;
	
	@JsonProperty(value="last", required=true)
	@JsonFormat(shape=Shape.STRING)
	private Date lastStamp;

	@JsonProperty(value="flows", required=true)
	private List<OptionsFlow> flows = new LinkedList<OptionsFlow>();
	
	public OptionsFlowCollection() {}
	
	public OptionsFlowCollection(Date firstStamp, Date lastStamp, Collection<OptionsFlow> flows) {
		this.firstStamp = firstStamp;
		this.lastStamp = lastStamp;
		
		if(flows != null)
			this.flows.addAll(flows);
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

	/**
	 * @return the flows
	 */
	public List<OptionsFlow> getFlows() {
		return flows;
	}

	/**
	 * @param flows the flows to set
	 */
	public void setFlows(List<OptionsFlow> flows) {
		this.flows = flows;
	}
}
