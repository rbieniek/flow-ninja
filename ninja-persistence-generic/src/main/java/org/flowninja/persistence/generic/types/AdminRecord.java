/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.types.generic.AdminKey;

/**
 * @author rainer
 *
 */
public class AdminRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 374142886913749300L;

	private String userName;
	private AdminKey key;
	private Set<AuthorityRecord> authorities;
	
	public AdminRecord() {}
	
	public AdminRecord(String userName, AdminKey key, Set<AuthorityRecord> authorities) {
		this.userName = userName;
		this.key = key;
		this.authorities = authorities;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
		
	/**
	 * @return the key
	 */
	public AdminKey getKey() {
		return key;
	}

	/**
	 * @return the authorities
	 */
	public Set<AuthorityRecord> getAuthorities() {
		return Collections.unmodifiableSet(authorities);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AdminRecord))
			return false;
		
		AdminRecord o = (AdminRecord)obj;
		
		return (new EqualsBuilder()).append(o.userName, this.userName)
				.append(o.key, this.key)
				.appendSuper(SetUtils.isEqualSet(o.authorities, this.authorities)).build();
	}
}
