/**
 * 
 */
package org.flowninja.collector.client.components;

import java.io.Serializable;
import java.time.Duration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class BearerToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857248279574643400L;
	
	private String token;
	private Duration timeToLive;

	public BearerToken() {}
	
	public BearerToken(String token, Duration timeToLive) {
		this.timeToLive = timeToLive;
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the timeToLive
	 */
	public Duration getTimeToLive() {
		return timeToLive;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BearerToken))
			return false;
		
		BearerToken o = (BearerToken)obj;
		
		return (new EqualsBuilder())
				.append(this.timeToLive, o.timeToLive)
				.append(this.token, o.token)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.timeToLive)
				.append(this.token)
				.toHashCode();
	}
}
