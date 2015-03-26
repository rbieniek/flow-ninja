/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

/**
 * Type of a record
 * 
 * @author rainer
 *
 */
public enum FieldType {
	IN_BYTES,
	IN_PKTS,
	FLOWS,
	PROTOCOL,
	TOS,
	TCP_FLAGS,
	L4_SRC_PORT,
	IPV4_SRC_ADDR,
	SRC_MASK,
	INPUT_SNMP,
	L4_DST_PORT,
	IPV4_DST_ADDR,
	DST_MASK,
	OUTPUT_SNMP,
	IPV4_NEXT_HOP,
	SRC_AS,
	DST_AS,
	BGP_IPV4_NEXT_HOP,
	MUL_DST_PKTS,
	MUL_DST_BYTES,
	LAST_SWITCHED,
	FIRST_SWITCHED,
	OUT_BYTES,
	OUT_PKTS,
	IPV6_SRC_ADDR,
	IPV6_DST_ADDR,
	IPV6_SRC_MASK,
	IPV6_DST_MASK,
	IPV6_FLOW_LABEL,
	ICMP_TYPE,
	MUL_IGMP_TYPE,
	SAMPLING_INTERVAL,
	SAMPLING_ALGORITHM,
	FLOW_ACTIVE_TIMEOUT,
	FLOW_INACTIVE_TIMEOUT,
	ENGINE_TYPE,
	ENINGE_ID,
	TOTAL_BYTES_EXP,
	TOTAL_PKTS_EXP,
	TOTAL_FLOWS_EXP,
	MPLS_TOP_LABEL_TYPE,
	MPLS_TOP_LABEL_IP_ADDR,
	FLOW_SAMPLER_ID,
	FLOW_SAMPLER_MODE,
	FLOW_SAMPLER_RANDOM_INTERVAL,
	DST_TOS,
	SRC_MAC,
	DST_MAC,
	SRC_VLAN,
	DST_VLAN,
	IP_PROTOCOL_VERSION,
	DIRECTION,
	IPV6_NEXT_HOP,
	BGP_IPV6_NEXT_HOP,
	IPV6_OPTION_HEADERS,
	MPLS_LABEL_1,
	MPLS_LABEL_2,
	MPLS_LABEL_3,
	MPLS_LABEL_4,
	MPLS_LABEL_5,
	MPLS_LABEL_6,
	MPLS_LABEL_7,
	MPLS_LABEL_8,
	MPLS_LABEL_9,
	MPLS_LABEL_10,
	PROPRIETARY, 
	EXTENSION;
	
	/**
	 * Map from integer type code to enum value
	 * 
	 * @param code integer to map from
	 * @return the enum value. Unknown codes are mapped to {@code EXTENSION}
	 */
	public static final FieldType fromCode(int code) {
		switch(code) {
		case 1:
			return IN_BYTES;
		case 2:
			return IN_PKTS;
		case 3:
			return FLOWS;
		case 4:
			return PROTOCOL;
		case 5:
			return TOS;
		case 6:
			return TCP_FLAGS;
		case 7:
			return L4_SRC_PORT;
		case 8:
			return IPV4_SRC_ADDR;
		case 9:
			return SRC_MASK;
		case 10:
			return INPUT_SNMP;
		case 11:
			return L4_DST_PORT;
		case 12:
			return IPV4_DST_ADDR;
		case 13:
			return DST_MASK;
		case 14:
			return OUTPUT_SNMP;
		case 15:
			return IPV4_DST_ADDR;
		case 16:
			return SRC_AS;
		case 17:
			return DST_AS;
		case 18:
			return BGP_IPV4_NEXT_HOP;
		case 19:
			return MUL_DST_PKTS;
		case 20:
			return MUL_DST_BYTES;
		case 21:
			return LAST_SWITCHED;
		case 22:
			return FIRST_SWITCHED;
		case 23:
			return OUT_BYTES;
		case 24:
			return OUT_PKTS;
		case 25:
		case 26:
			return PROPRIETARY;
		case 27:
			return IPV6_SRC_ADDR;
		case 28:
			return IPV6_DST_ADDR;
		case 29:
			return IPV6_SRC_MASK;
		case 30:
			return IPV6_DST_MASK;
		case 31:
			return IPV6_FLOW_LABEL;
		case 32:
			return ICMP_TYPE;
		case 33:
			return MUL_IGMP_TYPE;
		case 34:
			return SAMPLING_INTERVAL;
		case 35:
			return SAMPLING_ALGORITHM;
		case 36:
			return FLOW_ACTIVE_TIMEOUT;
		case 37:
			return FLOW_INACTIVE_TIMEOUT;
		case 38:
			return ENGINE_TYPE;
		case 39:
			return ENINGE_ID;
		case 40:
			return TOTAL_BYTES_EXP;
		case 41:
			return TOTAL_PKTS_EXP;
		case 42:
			return TOTAL_FLOWS_EXP;
		case 43:
		case 44:
		case 45:
			return PROPRIETARY;
		case 46:
			return MPLS_TOP_LABEL_TYPE;
		case 47:
			return MPLS_TOP_LABEL_IP_ADDR;
		case 48:
			return FLOW_SAMPLER_ID;
		case 49:
			return FLOW_SAMPLER_MODE;
		case 50:
			return FLOW_SAMPLER_RANDOM_INTERVAL;
		case 51:
		case 52:
		case 53:
		case 54:
			return PROPRIETARY;
		case 55:
			return DST_TOS;
		case 56:
			return SRC_MAC;
		case 57:
			return DST_MAC;
		case 58:
			return SRC_VLAN;
		case 59:
			return DST_VLAN;
		case 60:
			return IP_PROTOCOL_VERSION;
		case 61:
			return DIRECTION;
		case 62:
			return IPV6_NEXT_HOP;
		case 63:
			return BGP_IPV6_NEXT_HOP;
		case 64:
			return IPV6_OPTION_HEADERS;
		case 65:
		case 66:
		case 67:
		case 68:
		case 69:
			return PROPRIETARY;
		case 70:
			return MPLS_LABEL_1;
		case 71:
			return MPLS_LABEL_2;
		case 72:
			return MPLS_LABEL_3;
		case 73:
			return MPLS_LABEL_4;
		case 74:
			return MPLS_LABEL_5;
		case 75:
			return MPLS_LABEL_6;
		case 76:
			return MPLS_LABEL_7;
		case 77:
			return MPLS_LABEL_8;
		case 78:
			return MPLS_LABEL_9;
		case 79:
			return MPLS_LABEL_10;
		default:
			return EXTENSION;
		}		
	}
	
	/**
	 * Determine if field type is variable sized
	 * 
	 * @return
	 */
	public boolean isVariableSized() {
		switch(this) {
		case IN_BYTES:
		case IN_PKTS:
		case FLOWS:
		case INPUT_SNMP:
		case OUTPUT_SNMP:
		case SRC_AS:
		case DST_AS:
		case MUL_DST_BYTES:
		case MUL_DST_PKTS:
		case OUT_BYTES:
		case OUT_PKTS:
		case TOTAL_BYTES_EXP:
		case TOTAL_PKTS_EXP:
		case TOTAL_FLOWS_EXP:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Determine if field type is variable sized
	 * 
	 * @return
	 */
	public boolean isCounterLike() {
		switch(this) {
		case IN_BYTES:
		case IN_PKTS:
		case FLOWS:
		case MUL_DST_BYTES:
		case MUL_DST_PKTS:
		case OUT_BYTES:
		case OUT_PKTS:
		case TOTAL_BYTES_EXP:
		case TOTAL_PKTS_EXP:
		case TOTAL_FLOWS_EXP:
			return true;
		default:
			return false;
		}
	}
}
