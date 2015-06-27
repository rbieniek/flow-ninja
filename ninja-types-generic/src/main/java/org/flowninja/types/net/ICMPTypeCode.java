/**
 * 
 */
package org.flowninja.types.net;

/**
 * @author rainer
 *
 */
public class ICMPTypeCode {
	private ICMPType type;
	private ICMPCode code;
	
	private ICMPTypeCode(ICMPType type, ICMPCode code) {
		this.code = code;
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ICMPType getType() {
		return type;
	}

	/**
	 * @return the code
	 */
	public ICMPCode getCode() {
		return code;
	}
}
