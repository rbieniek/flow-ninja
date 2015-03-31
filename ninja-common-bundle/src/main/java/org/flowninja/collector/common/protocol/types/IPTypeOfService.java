/**
 * 
 */
package org.flowninja.collector.common.protocol.types;

/**
 * @author rainer
 *
 */
public enum IPTypeOfService {
	CS0, CS1, CS2, CS3, CS4, CS5, CS6, CS7,
	AF11, AF12, AF13, AF21, AF22, AF23, AF31, AF32, AF33, AF41, AF42, AF43,
	EF_PHB, VOICE_ADMINT, 
	RESERVED;
	
	public static final IPTypeOfService fromCode(int code) {
		switch(code) {
		case 0:
			return CS0;
		case 8:
			return CS1;
		case 16:
			return CS2;
		case 24:
			return CS3;
		case 32:
			return CS4;
		case 40:
			return CS5;
		case 48:
			return CS6;
		case 56:
			return CS7;
		case 10:
			return AF11;
		case 12:
			return AF12;
		case 14:
			return AF13;
		case 18:
			return AF21;
		case 20:
			return AF22;
		case 22:
			return AF23;
		case 26:
			return AF31;
		case 28:
			return AF32;
		case 30:
			return AF33;
		case 34:
			return AF41;
		case 36:
			return AF42;
		case 38:
			return AF43;
		default:
			return RESERVED;
		}
	}
}
