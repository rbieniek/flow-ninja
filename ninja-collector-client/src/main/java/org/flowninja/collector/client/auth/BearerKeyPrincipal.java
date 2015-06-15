/**
 * 
 */
package org.flowninja.collector.client.auth;

import java.io.Serializable;
import java.security.Principal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class BearerKeyPrincipal implements Principal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1095410776853109803L;

	private String key;
	
	public BearerKeyPrincipal() {
	}
	
	public BearerKeyPrincipal(String key) {
		this.key = key;
	}
	
	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return key;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(key)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BearerKeyPrincipal))
			return false;
		
		BearerKeyPrincipal o = (BearerKeyPrincipal)obj;
		
		return (new EqualsBuilder())
				.append(this.key, o.key)
				.isEquals();
	}
}
