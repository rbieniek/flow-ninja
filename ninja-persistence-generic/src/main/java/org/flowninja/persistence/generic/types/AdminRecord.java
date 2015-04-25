/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
}
