/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.types.generic.AccountingGroupKey;

/**
 * @author rainer
 *
 */
public class AccountingGroupRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7880413552771633953L;

	private AccountingGroupKey key;
	private String name;
	private String comment;
	private LocalDateTime createdWhen;
	private LocalDateTime lastModifiedAt;
	
	/**
	 * 
	 */
	public AccountingGroupRecord() {
	}

	public AccountingGroupRecord(AccountingGroupKey key, String name, String comment, LocalDateTime createdWhen, LocalDateTime lastModifiedAt) {
		this.key = key;
		this.name = name;
		this.comment = comment;
		this.createdWhen = createdWhen;
		this.lastModifiedAt = lastModifiedAt;
	}
	
	/**
	 * @return the key
	 */
	public AccountingGroupKey getKey() {
		return key;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AccountingGroupRecord))
			return false;
		
		AccountingGroupRecord o = (AccountingGroupRecord)obj;
		
		return (new EqualsBuilder())
				.append(this.key, o.key)
				.append(this.name, o.name)
				.append(this.comment, o.comment)
				.append(this.createdWhen, o.createdWhen)
				.append(this.lastModifiedAt, o.lastModifiedAt)
				.isEquals();
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
