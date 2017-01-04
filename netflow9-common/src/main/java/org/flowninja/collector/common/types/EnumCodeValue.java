/**
 * 
 */
package org.flowninja.collector.common.types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Container class for returning a enum alongside with it's raw code value
 * @author rainer
 *
 */
public class EnumCodeValue<T extends Enum<?>> {
	private T value;
	private int code;
	
	public EnumCodeValue(T  value, int code) {
		this.value = value;
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(!(obj instanceof EnumCodeValue))
			return false;
		
		EnumCodeValue<T> o = (EnumCodeValue<T>)obj;
		
		return (new EqualsBuilder()).append(this.value, o.value).append(this.code, o.code).isEquals();
	}
	
}
