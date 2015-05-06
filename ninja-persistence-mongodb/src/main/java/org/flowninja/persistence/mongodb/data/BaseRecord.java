/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.flowninja.types.generic.KeyBase;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class BaseRecord<T extends KeyBase> implements Serializable, Persistable<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150725750434855047L;
	
	@Id private T key;
	@CreatedDate private LocalDateTime createdWhen;
	@LastModifiedDate private LocalDateTime lastModifiedAt;
	
	/**
	 * 
	 */
	protected BaseRecord() {
	}
	
	protected BaseRecord(T key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public final T getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public final void setKey(T key) {
		this.key = key;
	}

	/**
	 * @return the createdWhen
	 */
	public final LocalDateTime getCreatedWhen() {
		return createdWhen;
	}

	/**
	 * @return the lastModifiedAt
	 */
	public final LocalDateTime getLastModifiedAt() {
		return lastModifiedAt;
	}

	@Override
	public final T getId() {
		return key;
	}

	@Override
	public final boolean isNew() {
		return (createdWhen == null);
	}

}
