/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class MongoAuthorityRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4958120169424467597L;
	
	@Id private AuthorityKey key;
	@Indexed private String authority;
	@CreatedDate private LocalDateTime createdWhen;
	@LastModifiedDate private LocalDateTime lastModifiedAt;
	
	public MongoAuthorityRecord() {}
	
	public MongoAuthorityRecord(String authority, AuthorityKey key) {
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
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @return the key
	 */
	public AuthorityKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(AuthorityKey key) {
		this.key = key;
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
