/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;

import org.flowninja.types.generic.AccountingGroupKey;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoAccountingGroupRecord extends BaseRecord<AccountingGroupKey> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6745207249161116942L;

	@Indexed private String name;
	private String comment;
	
	/**
	 * 
	 */
	public MongoAccountingGroupRecord() {
	}
	
	public MongoAccountingGroupRecord(AccountingGroupKey key, String name, String comment) {
		super(key);
		
		this.name = name;
		this.comment = comment;
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

}
