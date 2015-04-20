/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
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
	private Set<String> authorities;
	
	public AdminRecord() {}
	
	public AdminRecord(String userName, AdminKey key, Set<String> authorities) {
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
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the key
	 */
	public AdminKey getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(AdminKey key) {
		this.key = key;
	}


	/**
	 * @return the authorities
	 */
	public Set<String> getAuthorities() {
		return authorities;
	}


	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
