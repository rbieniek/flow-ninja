/**
 * 
 */
package org.flowninja.collector.common.protocol.types;

/**
 * IP Protocol definition
 * 
 * @author rainer
 *
 */
public enum IPProtocol {
	HOPOPT,
	ICMP,
	IGMP,
	GGP,
	IPv4,
	ST,
	TCP,
	CBT,
	EGP,
	IGP,
	BBC_RCC_MON,
	NVP2,
	PUP,
	ARGUS,
	EMCON,
	XNET,
	CHAOS,
	UDP,
	MUX,
	DCN_MEAS,
	HMP,
	PRM,
	XNS_IDP,
	TRUNK_1,
	TRUNK_2,
	LEAF_1,
	LEAF_2,
	RDP,
	IRTP,
	ISO_TP4,
	NETBLT,
	MFE_NSP,
	MERIT_INP,
	DCCP,
	THREE_PC,
	IDPR,
	XTP,
	DDP,
	IDPR_CMTP,
	TPPP,
	IL,
	IPv6,
	SDRP,
	IPv6_ROUTE,
	IPv6_FRAG,
	IDRP,
	RSVP,
	GRE,
	DSR,
	BNA,
	ESP,
	AH,
	I_NLSP,
	SWIPE,
	NARP,
	MOBILE,
	TLSP,
	SKIP,
	IPv6_ICMP,
	IPv6_NONXT,
	IPv6_OPTS,
	CFTP,
	SAT_EXPAK,
	KRYPTOLAN,
	RVD,
	IPPC,
	SAT_MON,
	VISA,
	IPCV,
	CPNX,
	CPHB,
	WSN,
	PVP,
	BR_SAT_MON,
	SUN_ND,
	WB_MON,
	WB_EXPAK,
	ISO_IP,
	VMTP,
	SECURE_VMTP,
	VINES,
	TTP,
	IPTM,
	NSFNET_IGP,
	DGP,
	TCF,
	EIGRP,
	OSPFIGP,
	SPRITE_RPC,
	LARP,
	MTP,
	AX25,
	IPIP,
	MICP,
	SCC_SP,
	ETHERIP,
	ENCAP,
	GMTP,
	IFMP,
	PNNI,
	PIM,
	ARIS,
	SCPS,
	QNX,
	A_N,
	IPCOMP,
	SNP,
	COMPAQ_PEER,
	IPX_IN_IP,
	VRRP,
	PGM,
	L2TP,
	DDX,
	IATP,
	STP,
	SRP,
	UTI,
	SMP,
	SM,
	PTP,
	ISIS_OVER_IPv4,
	FIRE,
	CRTP,
	CRUDP,
	SSCOPMCE,
	IPLT,
	SPS,
	PIPE,
	SCTP,
	FC,
	RSVP_E2E_IGNORE,
	MOBILITY_HEADER,
	UDP_LITE,
	MPLS_IN_IP,
	MANET,
	HIP,
	SHIM6,
	WESP,
	ROHC,
	RESERVED;
	
	public static IPProtocol fromCode(int code) {
		switch(code) {
		case 0:
			return HOPOPT;
		case 1:
			return ICMP;
		case 2:
			return IGMP;
		case 3:
			return GGP;
		case 4:
			return IPv4;
		case 5:
			return ST;
		case 6:
			return TCP;
		case 7:
			return CBT;
		case 8:
			return EGP;
		case 9:
			return IGP;
		case 10:
			return BBC_RCC_MON;
		case 11:
			return NVP2;
		case 12:
			return PUP;
		case 13:
			return ARGUS;
		case 14:
			return EMCON;
		case 15:
			return XNET;
		case 16:
			return CHAOS;
		case 17:
			return UDP;
		case 18:
			return MUX;
		case 19:
			return DCN_MEAS;
		case 20:
			return HMP;
		case 21:
			return PRM;
		case 22:
			return XNS_IDP;
		case 23:
			return TRUNK_1;
		case 24:
			return TRUNK_2;
		case 25:
			return LEAF_1;
		case 26:
			return LEAF_2;
		case 27:
			return RDP;
		case 28:
			return IRTP;
		case 29:
			return ISO_TP4;
		case 30:
			return NETBLT;
		case 31:
			return MFE_NSP;
		case 32:
			return MERIT_INP;
		case 33:
			return DCCP;
		case 34:
			return THREE_PC;
		case 35:
			return IDPR;
		case 36:
			return XTP;
		case 37:
			return DDP;
		case 38:
			return IDPR_CMTP;
		case 39:
			return TPPP;
		case 40:
			return IL;
		case 41:
			return IPv6;
		case 42:
			return SDRP;
		case 43:
			return IPv6_ROUTE;
		case 44:
			return IPv6_FRAG;
		case 45:
			return IDRP;
		case 46:
			return RSVP;
		case 47:
			return GRE;
		case 48:
			return DSR;
		case 49:
			return BNA;
		case 50:
			return ESP;
		case 51:
			return AH;
		case 52:
			return I_NLSP;
		case 53:
			return SWIPE;
		case 54:
			return NARP;
		case 55:
			return MOBILE;
		case 56:
			return TLSP;
		case 57:
			return SKIP;
		case 58:
			return IPv6_ICMP;
		case 59:
			return IPv6_NONXT;
		case 60:
			return IPv6_OPTS;
		case 62:
			return CFTP;
		case 64:
			return SAT_EXPAK;
		case 65:
			return KRYPTOLAN;
		case 66:
			return RVD;
		case 67:
			return IPPC;
		case 69:
			return SAT_MON;
		case 70:
			return VISA;
		case 71:
			return IPCV;
		case 72:
			return CPNX;
		case 73:
			return CPHB;
		case 74:
			return WSN;
		case 75:
			return PVP;
		case 76:
			return BR_SAT_MON;
		case 77:
			return SUN_ND;
		case 78:
			return WB_MON;
		case 79:
			return WB_EXPAK;
		case 80:
			return ISO_IP;
		case 81:
			return VMTP;
		case 82:
			return SECURE_VMTP;
		case 83:
			return VINES;
		case 84:
			return TTP;
		case 85:
			return NSFNET_IGP;
		case 86:
			return DGP;
		case 87:
			return TCF;
		case 88:
			return EIGRP;
		case 89:
			return OSPFIGP;
		case 90:
			return SPRITE_RPC;
		case 91:
			return LARP;
		case 92:
			return MTP;
		case 93:
			return AX25;
		case 94:
			return IPIP;
		case 95:
			return MICP;
		case 96:
			return SCC_SP;
		case 97:
			return ETHERIP;
		case 98:
			return ENCAP;
		case 100:
			return GMTP;
		case 101:
			return IFMP;
		case 102:
			return PNNI;
		case 103:
			return PIM;
		case 104:
			return ARIS;
		case 105:
			return SCPS;
		case 106:
			return QNX;
		case 107:
			return A_N;
		case 108:
			return IPCOMP;
		case 109:
			return SNP;
		case 110:
			return COMPAQ_PEER;
		case 111:
			return IPX_IN_IP;
		case 112:
			return VRRP;
		case 113:
			return PGM;
		case 115:
			return L2TP;
		case 116:
			return DDX;
		case 117:
			return IATP;
		case 118:
			return STP;
		case 119:
			return SRP;
		case 120:
			return UTI;
		case 121:
			return SMP;
		case 122:
			return SM;
		case 123:
			return PTP;
		case 124:
			return ISIS_OVER_IPv4;
		case 125:
			return FIRE;
		case 126:
			return CRTP;
		case 127:
			return CRUDP;
		case 128:
			return SSCOPMCE;
		case 129:
			return  IPLT;
		case 130:
			return SPS;
		case 131:
			return PIPE;
		case 132:
			return SCTP;
		case 133:
			return FC;
		case 134:
			return RSVP_E2E_IGNORE;
		case 135:
			return MOBILITY_HEADER;
		case 136:
			return UDP_LITE;
		case 137:
			return MPLS_IN_IP;
		case 138:
			return MANET;
		case 139:
			return HIP;
		case 140:
			return SHIM6;
		case 141:
			return WESP;
		case 142:
			return ROHC;
		
		default:
			return RESERVED;
		}
	}
}
