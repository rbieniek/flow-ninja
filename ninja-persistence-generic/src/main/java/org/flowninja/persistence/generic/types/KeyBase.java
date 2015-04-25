/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author rainer
 *
 */
public class KeyBase implements Serializable, Comparable<KeyBase> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1216374337037205388L;
	
	private UUID uuid;
	
	protected KeyBase() {}
	
	protected KeyBase(UUID uuid) {
		this.uuid = uuid;
	}
	
	protected KeyBase(String uuid) {
		this.uuid = UUID.fromString(uuid);
	}

	@Override
	public final int hashCode() {
		return (new HashCodeBuilder()).append(this.uuid).toHashCode();
	}

	@Override
	public final boolean equals(Object o) {
		if(!getClass().equals(o.getClass()))
			return false;
		
		return (new EqualsBuilder())
				.append(this.uuid, ((KeyBase)o).uuid)
				.isEquals();
	}
	
	@Override
	public int compareTo(KeyBase o) {
		return (new CompareToBuilder()).append(this.uuid, o.uuid).toComparison();
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	@Override
	@JsonValue
	public String toString() {
		return this.uuid != null ? this.uuid.toString() : null;
	}
}
