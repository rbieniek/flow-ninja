/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.flowninja.types.generic.AdminKey;
import org.flowninja.types.generic.AuthorityKey;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoAdminRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8487073960518576657L;
	
	@Id private AdminKey key;
	@Indexed private String userName;
	private String passwordHash;
	private Set<AuthorityKey> authorities = new HashSet<AuthorityKey>();
	@CreatedDate private LocalDateTime createdWhen;
	@LastModifiedDate private LocalDateTime lastModifiedAt;
	
	public MongoAdminRecord() {}
		
	public MongoAdminRecord(AdminKey key, String userName, String passwordHash, Set<AuthorityKey> authorities) {
		this.userName = userName;
		this.key = key;
		this.passwordHash = passwordHash;
		this.authorities = authorities;
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
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}
	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @return the authorities
	 */
	public Set<AuthorityKey> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(Set<AuthorityKey> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the createdWhen
	 */
	public LocalDateTime getCreatedWhen() {
		return createdWhen;
	}

	/**
	 * @return the lastModifiedAt
	 */
	public LocalDateTime getLastModifiedAt() {
		return lastModifiedAt;
	}
	
}
