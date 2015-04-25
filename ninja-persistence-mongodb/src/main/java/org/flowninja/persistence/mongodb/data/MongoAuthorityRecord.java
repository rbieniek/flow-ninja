/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;

import org.flowninja.persistence.generic.types.AuthorityKey;
import org.springframework.data.annotation.Id;
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
}
