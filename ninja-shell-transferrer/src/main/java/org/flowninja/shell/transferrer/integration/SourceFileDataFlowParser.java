/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.flowninja.types.flows.FlowData;
import org.flowninja.types.flows.FlowDirection;
import org.flowninja.types.flows.FlowHeader;
import org.flowninja.types.flows.NetworkFlow;
import org.flowninja.types.net.ICMPCode;
import org.flowninja.types.net.ICMPType;
import org.flowninja.types.net.IGMPType;
import org.flowninja.types.net.IPProtocol;
import org.flowninja.types.net.IPProtocolVersion;
import org.flowninja.types.net.IPTypeOfService;
import org.flowninja.types.net.MPLSTopLabelType;
import org.flowninja.types.net.TCPFLags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

/**
 * @author rainer
 *
 */
@Component
public class SourceFileDataFlowParser implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(SourceFileDataFlowParser.class);
	
	private ObjectMapper mapper;
	
	private static class FlowDataMapper {
		private FlowData data = new FlowData();
		
		private BigInteger asBigInteger(JsonValue value) {
			return ((JsonNumber)value).bigIntegerValue();
		}

		private int asInt(JsonValue value) {
			return ((JsonNumber)value).intValue();
		}
		
		private String asString(JsonValue value) {
			return ((JsonString)value).getString();
		}

		private JsonValue propertyOf(JsonValue value, String propName) {
			return ((JsonObject)value).get(propName);
		}
		
		public void map(String name, JsonValue value) {
			switch(name) {
			case "IN_BYTES":
				data.setInputBytes(asBigInteger(value));
				break;
			case "IN_PKTS":
				data.setInputPkts(asBigInteger(value));
				break;
			case "FLOWS":
				data.setFlows(asBigInteger(value));
				break;
			case "PROTOCOL":
				data.setProtocol(IPProtocol.valueOf(asString(propertyOf(value, "value"))));
				break;
			case "SRC_TOS":
				data.setTypeOfService(IPTypeOfService.valueOf(asString(propertyOf(value, "value"))));
				break;
			case "TCP_FLAGS": {
					SortedSet<TCPFLags> flags = new TreeSet<TCPFLags>();
				
					((JsonArray)value).forEach((v) -> flags.add(TCPFLags.valueOf(asString(v))));
					
					data.setTcpFlags((new ArrayList<TCPFLags>(flags)).toArray(new TCPFLags[0]));
				}
				break;
			case "L4_SRC_PORT":
				data.setLayer4SourcePort(asInt(value));
				break;
			case "IPV4_SRC_ADDR":
				data.setIpv4SourceAddress(asString(value));
				break;
			case "SRC_MASK":
				data.setIpv4SourceMask(asInt(value));
				break;
			case "INPUT_SNMP":
				data.setInputSnmpIndex(asInt(value));
				break;
			case "L4_DST_PORT":
				data.setLayer4DestinationPort(asInt(value));
				break;
			case "IPV4_DST_ADDR":
				data.setIpv4DestinationAddress(asString(value));
				break;
			case "DST_MASK":
				data.setIpv4DestinationMask(asInt(value));
				break;
			case "OUTPUT_SNMP":
				data.setOutputSnmpIndex(asInt(value));
				break;
			case "IPV4_NEXT_HOP":
				data.setIpv4NextHopAddress(asString(value));
				break;
			case "SRC_AS":
				data.setSourceAs(asInt(value));
				break;
			case "DST_AS":
				data.setDestinationAs(asInt(value));
				break;
			case "BGP_IPV4_NEXT_HOP":
				data.setIpv4BgpNextHopAddress(asString(value));
				break;
			case "MUL_DST_PKTS":
				data.setMulticastDestinationPackets(asBigInteger(value));
				break;
			case "MUL_DST_BYTES":
				data.setMulticastDestinationBytes(asBigInteger(value));
				break;
			case "LAST_SWITCHED":
				data.setLastSwitched(asInt(value));
				break;
			case "FIRST_SWITCHED":
				data.setFirstSwitched(asInt(value));
				break;
			case "OUT_BYTES":
				data.setOutputBytes(asBigInteger(value));
				break;
			case "OUT_PKTS":
				data.setOutputPkts(asBigInteger(value));
				break;
			case "MIN_PKT_LNGTH":
				data.setMinimumPacketLength(asInt(value));
				break;
			case "MAX_PKT_LNGTH":
				data.setMaximumPacketLength(asInt(value));
				break;
			case "IPV6_SRC_ADDR":
				data.setIpv6SourceAddress(asString(value));
				break;
			case "IPV6_DST_ADDR":
				data.setIpv6DestinationAddress(asString(value));
				break;
			case "IPV6_SRC_MASK":
				data.setIpv6SourceMask(asInt(value));
				break;
			case "IPV6_DST_MASK":
				data.setIpv6DestinationMask(asInt(value));
				break;
			case "IPV6_FLOW_LABEL":
				data.setIpv6FlowLabel(asInt(value));
				break;
			case "ICMP_TYPE":
				data.setIcmpType(ICMPType.valueOf(asString(propertyOf(value, "type"))));
				data.setIcmpCode(ICMPCode.valueOf(asString(propertyOf(value, "code"))));
				break;
			case "MUL_IGMP_TYPE":
				data.setIgmpType(IGMPType.valueOf(asString(value)));
				break;
			case "IPV4_SRC_PREFIX":
				break;
			case "IPV4_DST_PREFIX":
				break;
			case "MPLS_TOP_LABEL_TYPE":
				data.setMplsTopLabelType(MPLSTopLabelType.valueOf(asString(value)));
				break;
			case "MPLS_TOP_LABEL_IP_ADDR":
				data.setMplsTopLabelIpAddress(asString(value));
				break;
			case "FLOW_SAMPLER_ID":
				data.setFlowSamplerID(asInt(value));
				break;
			case "FLOW_SAMPLER_MODE":
				break;
			case "FLOW_CLASS":
				data.setFlowClass(asInt(value));
				break;
			case "MIN_TTL":
				data.setMinimumTimeToLive(asInt(value));
				break;
			case "MAX_TTL":
				data.setMaximumTimeToLive(asInt(value));
				break;
			case "IPV4_IDENT":
				data.setIpv4Identity(asInt(value));
				break;
			case "DST_TOS":
				data.setDestinationTypeOfService(IPTypeOfService.valueOf(asString(propertyOf(value, "value"))));
				break;
			case "SRC_MAC":
				break;
			case "DST_MAC":
				break;
			case "SRC_VLAN":
				data.setSourceVlan(asInt(value));
				break;
			case "DST_VLAN":
				data.setDestinationVlan(asInt(value));
				break;
			case "IP_PROTOCOL_VERSION":
				data.setIpProtocolVersion(IPProtocolVersion.valueOf(asString(value)));
				break;
			case "DIRECTION":
				data.setFlowDirection(FlowDirection.valueOf(asString(propertyOf(value, "value"))));
				break;
			case "IPV6_NEXT_HOP":
				data.setIpv6NextHopAddress(asString(value));
				break;
			case "BGP_IPV6_NEXT_HOP":
				data.setIpv6BgpNextHopAddress(asString(value));
				break;
			case "IPV6_OPTION_HEADERS":
				data.setIpv6OptionHeaders(asInt(value));
				break;
			case "MPLS_LABEL_1":
				data.setMplsLabel1(asInt(value));
				break;
			case "MPLS_LABEL_2":
				data.setMplsLabel2(asInt(value));
				break;
			case "MPLS_LABEL_3":
				data.setMplsLabel3(asInt(value));
				break;
			case "MPLS_LABEL_4":
				data.setMplsLabel4(asInt(value));
				break;
			case "MPLS_LABEL_5":
				data.setMplsLabel5(asInt(value));
				break;
			case "MPLS_LABEL_6":
				data.setMplsLabel6(asInt(value));
				break;
			case "MPLS_LABEL_7":
				data.setMplsLabel7(asInt(value));
				break;
			case "MPLS_LABEL_8":
				data.setMplsLabel8(asInt(value));
				break;
			case "MPLS_LABEL_9":
				data.setMplsLabel9(asInt(value));
				break;
			case "MPLS_LABEL_10":
				data.setMplsLabel10(asInt(value));
				break;
			case "IN_DST_MAC":
				break;
			case "OUT_SRC_MAC":
				break;
			case "IN_PERMANENT_BYTES":
				break;
			case "IN_PERMANENT_PKTS":
				break;
			case "FRAGMENT_OFFSET":
				break;
			case "FORWARDING_STATUS":
				break;
			case "MPLS_PAL_RD":
				break;
			case "MPLS_PREFIX_LEN":
				break;
			case "SRC_TRAFFIC_INDEX":
				break;
			case "DST_TRAFFIC_INDEX":
				break;
			case "APPLICATION_DESCRIPTION":
				break;
			case "APPLICATION_TAG":
				break;
			case "APPLICATION_NAME":
				break;
			case "POST_IP_DIFF_SERV_CODE_POINT":
				break;
			case "REPLICATION_FACTOR":
				break;
			case "L2_PKT_SECT_OFFSET":
				break;
			case "L2_PKT_SECT_SIZE":
				break;
			case "L2_PKT_SECT_DATA":
				break;
			}
		}

		/**
		 * @return the data
		 */
		public FlowData getData() {
			return data;
		}

	}
	
	@Transformer(inputChannel="sourceDataFileChannel", outputChannel="sourceDataFlowChannel")
	public List<NetworkFlow> parseDataFlows(Message<File> flowFile) throws IOException {
		List<NetworkFlow> flows = new LinkedList<NetworkFlow>();
		LineNumberReader lnr = null;
		String currentLine;
		
		try {
			lnr = new LineNumberReader(new FileReader(flowFile.getPayload()));
			
			while((currentLine = lnr.readLine()) != null) {
				JsonObject root = mapper.readValue(new StringReader(currentLine), JsonObject.class);

				flows.add(parseFlowRoot(root));
			}
		} catch(IOException e) {
			logger.error("failed to parse data flows file {}", flowFile.getPayload().getAbsolutePath(), e);
			
			throw e;
		} catch(RuntimeException e) {
			logger.error("failed to parse data flows file {}", flowFile.getPayload().getAbsolutePath(), e);
			
			throw e;
		} finally {
			if(lnr != null) {
				try {
					lnr.close();
				} catch(IOException e) {
					
				}
			}
		}
		
		return flows;
	}

	private NetworkFlow parseFlowRoot(JsonObject root) {
		NetworkFlow flow = new NetworkFlow();
		
		flow.setPeerIp(root.getString("peer"));
		flow.setFlowUuid(root.getString("uuid"));
		
		flow.setHeader(parseFlowHeader(root.getJsonObject("header")));
		
		if(root.containsKey("flow")) {
			FlowDataMapper flowMapper = new FlowDataMapper();
			
			root.getJsonObject("flow").forEach((n,v) -> flowMapper.map(n,v));
			
			flow.setData(flowMapper.getData());
		}
		
		return flow;
	}
	
	private FlowHeader parseFlowHeader(JsonObject header)  {
		if(header == null)
			throw new IllegalArgumentException("null header passed");
		
		JsonObject timestamp = header.getJsonObject("timestamp");
		
		if(timestamp == null)
			throw new IllegalArgumentException("null timestamp passed");
		
		return new FlowHeader(
				header.getInt("recordCount"),
				header.getInt("sysUpTime"),
				timestamp.getInt("value"),
				header.getInt("sequenceNumber"),
				header.getInt("sourceId"));
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		mapper = new ObjectMapper();
		mapper.registerModule(new JSR353Module());
	}
}
