/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class AuthorityRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161111824477501729L;

	private String authority;
	private AuthorityKey key;
	
	public AuthorityRecord() {}
	
	public AuthorityRecord(String authority, AuthorityKey key) {
		this.authority = authority;
		this.key = key;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @return the key
	 */
	public AuthorityKey getKey() {
		return key;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(authority)
				.append(key)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AuthorityRecord))
			return false;
		
		AuthorityRecord o = (AuthorityRecord)obj;
		
		return (new EqualsBuilder())
				.append(this.authority, o.authority)
				.append(this.key, o.key)
				.isEquals();
	}
}
