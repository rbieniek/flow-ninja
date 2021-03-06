/**
 * 
 */
package org.flowninja.types.net;

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
	RESERVED_EXPERIMENTATION
}
