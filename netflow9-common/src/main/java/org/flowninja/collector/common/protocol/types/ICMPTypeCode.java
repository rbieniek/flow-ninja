/**
 *
 */
package org.flowninja.collector.common.protocol.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ICMPTypeCode {
	private ICMPType type;
	private ICMPCode code;

	@SuppressWarnings("incomplete-switch")
	public static ICMPTypeCode fromCodes(final int typeValue, final int codeValue) {
		ICMPType type = ICMPType.fromCode(typeValue);
		ICMPCode code = ICMPCode.UNASSIGNED;

		switch (type) {
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