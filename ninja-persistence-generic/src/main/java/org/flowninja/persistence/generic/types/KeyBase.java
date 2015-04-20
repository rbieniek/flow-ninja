/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.io.Serializable;
import java.util.UUID;

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
		return this.uuid.hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		if(!getClass().equals(o.getClass()))
			return false;
		
		return uuid.equals(((KeyBase)o).uuid);
	}
	
	@Override
	public int compareTo(KeyBase o) {
		return uuid.compareTo(o.uuid);
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
		return this.uuid.toString();
	}
}
