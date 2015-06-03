/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;

import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoCollectorRecord extends BaseRecord<CollectorKey> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8544784353616386073L;

	@Indexed private AccountingGroupKey groupKey;
	@Indexed private String name;
	private String comment;
	@Indexed private String clientID;
	private String clientSecret;
	
	/**
	 * 
	 */
	public MongoCollectorRecord() {
	}

	/**
	 * @param key
	 */
	public MongoCollectorRecord(CollectorKey key, AccountingGroupKey groupKey, String name, String comment, String clientID, String clientSecret) {
		super(key);
		
		this.groupKey = groupKey;
		this.name = name;
		this.comment = comment;
		this.clientID = clientID;
		this.clientSecret = clientSecret;
	}

	/**
	 * @return the groupKey
	 */
	public AccountingGroupKey getGroupKey() {
		return groupKey;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @param clientID the clientID to set
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @param clientSecret the clientSecret to set
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}
