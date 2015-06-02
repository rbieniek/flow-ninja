/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.types.generic.CollectorKey;

/**
 * This class models the record for a data collector 
 * 	
 * @author rainer
 *
 */
public class CollectorRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 802635600157788600L;

	private CollectorKey key;
	private AccountingGroupRecord accountingGroup;
	private String clientId;
	private String clientSecret;
	private String name;
	private String comment;
	private LocalDateTime createdWhen;
	private LocalDateTime lastModifiedAt;

	/**
	 * default constructor
	 */
	public CollectorRecord() {}
	
	/**
	 * default constructor
	 */
	public CollectorRecord(CollectorKey key, AccountingGroupRecord accountingGroup, 
			String name, String comment,  
			String clientId, String clientSecret, 
			LocalDateTime createdWhen, LocalDateTime lastModifiedAt) {
		this.key = key;
		this.accountingGroup = accountingGroup;
		this.name = name;
		this.comment = comment;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.createdWhen = createdWhen;
		this.lastModifiedAt = lastModifiedAt;
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
	 * @return the key
	 */
	public CollectorKey getKey() {
		return key;
	}

	/**
	 * @return the accountingGroup
	 */
	public AccountingGroupRecord getAccountingGroup() {
		return accountingGroup;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.key).append(this.accountingGroup)
				.append(this.name).append(this.comment)
				.append(this.clientId).append(this.clientSecret)
				.append(this.createdWhen).append(this.lastModifiedAt)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CollectorRecord))
			return false;
		
		CollectorRecord o = (CollectorRecord)obj;
		
		return (new EqualsBuilder())
				.append(this.key, o.key).append(this.accountingGroup, o.accountingGroup)
				.append(this.name, o.name).append(this.comment, o.comment)
				.append(this.clientId, o.clientId).append(this.clientSecret, o.clientSecret)
				.append(this.createdWhen, o.createdWhen).append(this.lastModifiedAt, o.lastModifiedAt)
				.isEquals();
	}
}
