/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;

import org.flowninja.types.generic.AuthorityKey;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoAuthorityRecord extends BaseRecord<AuthorityKey> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4958120169424467597L;
	
	@Indexed private String authority;
	
	public MongoAuthorityRecord() {}
	
	public MongoAuthorityRecord(AuthorityKey key, String authority) {
		super(key);
		
		this.authority = authority;
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
}
