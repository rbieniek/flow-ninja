/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

/**
 * Scope type for exported Netflow 9 metadata
 * 
 * @author rainer
 *
 */
public enum ScopeType {
	SYSTEM,
	INTERFACE,
	LINE_CARD,
	NETFLOW_CACHE,
	TEMPLATE,
	PROPIETARY;
	
	public static ScopeType fromCode(int code) {
		switch(code) {
			case 1:
				return SYSTEM;
			case 2:
				return INTERFACE;
			case 3:
				return LINE_CARD;
			case 4:
				return NETFLOW_CACHE;
			case 5: 
				return TEMPLATE;
			default:
				return PROPIETARY;
		}
	}
}
