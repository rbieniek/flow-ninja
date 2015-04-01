/**
 * 
 */
package org.flowninja.collector.common.types;

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
	
	
}
