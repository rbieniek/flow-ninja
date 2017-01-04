/**
 * 
 */
package org.flowninja.collector.common.protocol.types;

/**
 * @author rainer
 *
 */
public enum MPLSTopLabelType {
	UNKNOWN,
	TE_MIDPT,
	ATOM,
	VPN,
	BGP,
	LDP;
	
	public static MPLSTopLabelType fromCode(int code) {
		switch(code) {
		case 1:
			return TE_MIDPT;
		case 2:
			return ATOM;
		case 3:
			return VPN;
		case 4:
			return BGP;
		case 5:
			return LDP;
		default:
			return UNKNOWN;
		}
	}
}
