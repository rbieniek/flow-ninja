/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.flowninja.persistence.generic.types.AdminKey;
import org.springframework.data.annotation.Id;
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
	
	public MongoAdminRecord() {}
		
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
	
}
