/**
 * 
 */
package org.flowninja.collector.common.protocol.types;

/**
 * IMGP multicast packet types
 * 
 * @author rainer
 *
 */
public enum IGMPType {
	RESERVED,
	RESERVED_OBSOLETE,
	UNASSIGNED,
	IGMP_MEMBERSHIP_QUERY,
	IGMPv1_MEMBERSHIP_REPORT,
	DVMRP,
	PIMv1,
	CISCO_TRACE,
	IGMPv2_MEMBERSHIP_REPORT,
	IGMPv2_LEAVE_GROUP,
	MULTICAST_TRACEROUTE_RESPONSE,
	MULTICAST_TRACEROUTE,
	IGMPv3_MEMBERSHIP_REPORT,
	MULTICAST_ROUTER_ADVERTISEMENT,
	MULTICAST_ROUTER_SOLICITATION,
	MULTICAST_ROUTER_TERMINATION,
	RESERVED_EXPERIMENTATION;
	
	
	public static IGMPType fromCode(int code) {
		switch(code) {
		case 0:
			return RESERVED;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return RESERVED_OBSOLETE;
		case 17:
			return IGMP_MEMBERSHIP_QUERY;
		case 18:
			return IGMPv1_MEMBERSHIP_REPORT;
		case 19:
			return DVMRP;
		case 20:
			return PIMv1;
		case 21:
			return CISCO_TRACE;
		case 22:
			return IGMPv2_MEMBERSHIP_REPORT;
		case 23:
			return IGMPv2_LEAVE_GROUP;
		case 30:
			return MULTICAST_TRACEROUTE_RESPONSE;
		case 31:
			return MULTICAST_TRACEROUTE;
		case 34:
			return IGMPv3_MEMBERSHIP_REPORT;
		case 48:
			return MULTICAST_ROUTER_ADVERTISEMENT;
		case 49:
			return MULTICAST_ROUTER_SOLICITATION;
		case 50:
			return MULTICAST_ROUTER_TERMINATION;
		case 250:
		case 251:
		case 252:
		case 253:
		case 254:
			return RESERVED_EXPERIMENTATION;
		default:
			return UNASSIGNED;
		}
	}
}
