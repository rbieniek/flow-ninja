/**
 * 
 */
package org.flowninja.collector.common.protocol.types;

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
	
	@SuppressWarnings("incomplete-switch")
	public static ICMPTypeCode fromCodes(int typeValue, int codeValue) {
		ICMPType type = ICMPType.fromCode(typeValue);
		ICMPCode code = ICMPCode.UNASSIGNED;

		switch(type) {
		case DESTINATION_UNREACHABLE:
			code = ICMPCode.fromCodeForType3(codeValue);
			break;
		case REDIRECT:
			code = ICMPCode.fromCodeForType5(codeValue);
			break;
		case ALTERNATE_HOST_ADDRESS:
			code = ICMPCode.fromCodeForType6(codeValue);
			break;
		case ROUTER_ADVERTISEMENT:
			code = ICMPCode.fromCodeForType9(codeValue);
			break;
		case TIME_EXCEEDED:
			code = ICMPCode.fromCodeForType11(codeValue);
			break;
		case PARAMETER_PROBLEM:
			code = ICMPCode.fromCodeForType12(codeValue);
			break;
		case PHOTURIS:
			code = ICMPCode.fromCodeForType40(codeValue);
			break;
		}
		
		return new ICMPTypeCode(type, code);
	}
}
