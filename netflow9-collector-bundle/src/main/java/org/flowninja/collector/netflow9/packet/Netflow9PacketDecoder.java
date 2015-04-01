/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataFlowRecord;
import org.flowninja.collector.common.netflow9.types.EngineType;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowDirection;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.IPv6OptionHeaders;
import org.flowninja.collector.common.netflow9.types.OptionField;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlowRecord;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.SamplingAlgorithm;
import org.flowninja.collector.common.netflow9.types.ScopeField;
import org.flowninja.collector.common.netflow9.types.ScopeFlowRecord;
import org.flowninja.collector.common.netflow9.types.ScopeType;
import org.flowninja.collector.common.netflow9.types.Template;
import org.flowninja.collector.common.netflow9.types.TemplateField;
import org.flowninja.collector.common.protocol.types.ForwardingStatus;
import org.flowninja.collector.common.protocol.types.ICMPTypeCode;
import org.flowninja.collector.common.protocol.types.IGMPType;
import org.flowninja.collector.common.protocol.types.IPProtocol;
import org.flowninja.collector.common.protocol.types.IPProtocolVersion;
import org.flowninja.collector.common.protocol.types.IPTypeOfService;
import org.flowninja.collector.common.protocol.types.MPLSTopLabelType;
import org.flowninja.collector.common.protocol.types.TCPFLags;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.collector.common.types.CounterFactory;
import org.flowninja.collector.common.types.EncodedData;
import org.flowninja.collector.common.types.EnumCodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netwflow9 datagram packet decoder implementation. 
 * 
 * This decoder receives a byte-encoded netflow 9 packet
 * and sents a decoded packet object upstream.
 * 
 * The packet decoder temporarily stores received flow packet if a matching template record has not been received yet
 * 
 * @author rainer
 *
 */
public class Netflow9PacketDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(Netflow9PacketDecoder.class);
	
	private static final int PACKET_HEADER_LENGTH = 20;
	private static final int VERSION_NUMBER = 9;
	private static final int TEMPLATE_FLOWSET_ID = 0;
	private static final int OPTIONS_TEMPLATE_FLOWSET_ID = 1;
	
	private PeerRegistry peerRegistry;
	private PeerAddressMapper peerAddressMapper = new InetSocketAddressPeerAddressMapper();
	
	/**
	 * @param peerFlowRegistry the peerFlowRegistry to set
	 */
	public void setPeerRegistry(PeerRegistry peerFlowRegistry) {
		this.peerRegistry = peerFlowRegistry;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is active");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is inactive");
	}

	/**
	 * main packet decoder routine. 
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		InetAddress peerAddress = peerAddressMapper.mapRemoteAddress(ctx.channel().remoteAddress());
		
		logger.info("received flow packet from peer {}", peerAddress);
		
		if(in.readableBytes() > PACKET_HEADER_LENGTH) {
			int versionNumber = in.readUnsignedShort();
			
			if(versionNumber == VERSION_NUMBER) {
				Header header = new Header(in.readUnsignedShort(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt());

				List<Template> templates = new LinkedList<Template>();
				List<OptionsTemplate> optionsTemplates = new LinkedList<OptionsTemplate>();
				List<FlowBuffer> flows = new LinkedList<FlowBuffer>();
				
				int recordNumber = 0;
				while(in.readableBytes() > 4) {					
					int flowSetId = in.readUnsignedShort();
					int length = in.readUnsignedShort();
					int remainingOctets = length - 4; // subtract length of flowset ID and flowset length 
					
					if(in.readableBytes() < remainingOctets) {
						logger.error("packet short to {} bytes when {} bytes required in record {}", 
								in.readableBytes(), remainingOctets, recordNumber);

						return;
					}

					ByteBuf workBuf = in.readSlice(remainingOctets);
					
					switch(flowSetId) {
					case TEMPLATE_FLOWSET_ID:
						while(workBuf.readableBytes() > 4) {
							int flowSetID = workBuf.readUnsignedShort();
							int fieldCount = workBuf.readUnsignedShort();
							List<TemplateField> fields = new LinkedList<TemplateField>();
							
							if(workBuf.readableBytes() < fieldCount * 4) {
								logger.error("packet short to {} bytes when {} bytes required in record {}",
										workBuf.readableBytes(), fieldCount * 4, recordNumber);
								
								return;
							}
							
							for(int fieldNumber=0; fieldNumber < fieldCount; fieldNumber++) {
								fields.add(new TemplateField(FieldType.fromCode(workBuf.readUnsignedShort()), 
										workBuf.readUnsignedShort()));
							}
							
							templates.add(new Template(flowSetID, fields));								
							recordNumber++;
						}
						break;
					case OPTIONS_TEMPLATE_FLOWSET_ID: {
							int flowSetID = workBuf.readUnsignedShort();
							int scopeLength = workBuf.readUnsignedShort();
							int optionsLength = workBuf.readUnsignedShort();
							List<ScopeField> scopeFields = new LinkedList<ScopeField>();
							List<OptionField> optionFields = new LinkedList<OptionField>();
							
							while(scopeLength >= 4) {
								scopeFields.add(new ScopeField(ScopeType.fromCode(workBuf.readUnsignedShort()), 
										workBuf.readUnsignedShort()));
								scopeLength -= 4;
							}
							
							while(optionsLength >= 4) {
								optionFields.add(new OptionField(FieldType.fromCode(workBuf.readUnsignedShort()), 
										workBuf.readUnsignedShort()));
								optionsLength -= 4;
							}
							
							optionsTemplates.add(new OptionsTemplate(flowSetID, scopeFields, optionFields));
							
							recordNumber++;
						}
						break;
					default:
						flows.add(new FlowBuffer(flowSetId, workBuf));
					}
				}
				
				FlowRegistry flowRegistry = peerRegistry.registryForPeerAddress(peerAddress, header.getSourceId());				

				flowRegistry.addFlowTemplates(templates);
				flowRegistry.addOptionTemplates(optionsTemplates);
				
				flows.addAll(flowRegistry.backlogFlows());
				
				
				List<FlowBuffer> backlogFlows = new LinkedList<FlowBuffer>();
				
				for(FlowBuffer flowBuffer : flows) {
					Template template = null;
					OptionsTemplate optionsTemplate = null;
					
					if((template = flowRegistry.templateForFlowsetID(flowBuffer.getFlowSetId())) != null) {
						out.addAll(decodeDataTemplate(header, flowBuffer, template));
					} else if((optionsTemplate = flowRegistry.optionTemplateForFlowsetID(flowBuffer.getFlowSetId())) != null) {
						out.addAll(decodeOptionsTemplate(header, flowBuffer, optionsTemplate));						
					} else {
						backlogFlows.add(flowBuffer);
					}
				}
				
				flowRegistry.backlogFlows(backlogFlows);
			} 
		} else {
			logger.error("dropping received packet with {} bytes size but expected at least {} bytes", 
					in.readableBytes(), PACKET_HEADER_LENGTH);
		}
	}

	/**
	 * Decode a data flow from a data buffer and a given template
	 * 
	 * @param header
	 * @param flowBuffer
	 * @param template
	 * @return
	 */
	private List<DataFlow> decodeDataTemplate(Header header, FlowBuffer flowBuffer, Template template) {
		ByteBuf buffer = flowBuffer.getBuffer();
		List<DataFlow> flows = new LinkedList<DataFlow>();
		int dataLength = template.getTemplateLength();

		while(buffer.readableBytes() >= dataLength) {
			List<DataFlowRecord> flowRecords = new LinkedList<DataFlowRecord>();

			for(TemplateField field : template.getFields()) {
				flowRecords.add(new DataFlowRecord(field.getType(), decodeValue(field.getType(), field.getLength(), buffer)));
			}
			
			flows.add(new DataFlow(header, flowRecords));
		}
		
		return flows;
	}

	/**
	 * Decode a data flow from a data buffer and a given template
	 * 
	 * @param header
	 * @param flowBuffer
	 * @param template
	 * @return
	 */
	private List<OptionsFlow> decodeOptionsTemplate(Header header, FlowBuffer flowBuffer, OptionsTemplate template) {
		ByteBuf buffer = flowBuffer.getBuffer();
		int dataLength = template.getTemplateLength();
		List<OptionsFlow> optionsFlows = new LinkedList<OptionsFlow>();
		
		while(buffer.readableBytes() >= dataLength) {
			List<OptionsFlowRecord> flowRecords = new LinkedList<OptionsFlowRecord>();
			List<ScopeFlowRecord> scopeRecords = new LinkedList<ScopeFlowRecord>();		

			for(ScopeField field : template.getScopeFields()) {
				Counter value = null;
				
				if(field.getLength() > 0) {
					byte data[] = new byte[field.getLength()];
					
					buffer.readBytes(data);
					
					value = CounterFactory.decode(data);
				}
				
				scopeRecords.add(new ScopeFlowRecord(field.getType(), value));
			}
			
			for(OptionField field : template.getOptionFields()) {
				flowRecords.add(new OptionsFlowRecord(field.getType(), decodeValue(field.getType(), field.getLength(), buffer)));
			}
			
		 optionsFlows.add(new OptionsFlow(header, scopeRecords, flowRecords));
		}
		
		return optionsFlows;
	}

	/**
	 * 
	 * @param type
	 * @param buffer
	 * @return
	 */
	private Object decodeValue(FieldType type, int length, ByteBuf buffer) {
		Object value = null;
		
		switch(type) {
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
		case IN_PERMANENT_BYTES:
		case IN_PERMANENT_PKTS:
		case INPUT_SNMP: 
		case OUTPUT_SNMP:
		case L2_PKT_SECT_OFFSET:
		case L2_PKT_SECT_SIZE:
		    {
				byte[] dst = new byte[length];
				
				buffer.readBytes(dst);
				value = CounterFactory.decode(dst);
			}
			break;
		case PROTOCOL:
			{
				int code = buffer.readUnsignedByte();
				value = new EnumCodeValue<IPProtocol>(IPProtocol.fromCore(code), code);
			}
			break;
		case SRC_TOS:
		case DST_TOS:
		case POST_IP_DIFF_SERV_CODE_POINT:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<IPTypeOfService>(IPTypeOfService.fromCode(code), code);
			}
			break;
		case TCP_FLAGS:
			if(length == 1)
				value = TCPFLags.fromCode(buffer.readUnsignedByte());
			else if(length == 2) 
				value = TCPFLags.fromCode(buffer.readUnsignedShort());
			else {
				buffer.skipBytes(length);
				logger.warn("recieved type {} field with unsupported length {}", type, length);
			}
			break;
		case L4_SRC_PORT:
		case L4_DST_PORT:
		case MIN_PKT_LNGTH:
		case MAX_PKT_LNGTH:
		case FLOW_ACTIVE_TIMEOUT:
		case FLOW_INACTIVE_TIMEOUT:
		case IPV4_IDENT:
		case SRC_VLAN:
		case DST_VLAN:
		case FRAGMENT_OFFSET:
			value = new Integer(buffer.readUnsignedShort());
			break;
		case IPV4_SRC_ADDR:
		case IPV4_DST_ADDR: 
		case IPV4_DST_PREFIX:
		case IPV4_SRC_PREFIX:
		case IPV4_NEXT_HOP:
		case MPLS_TOP_LABEL_IP_ADDR:
		case BGP_IPV4_NEXT_HOP: {
				byte[] tmp = new byte[4];
				
				buffer.readBytes(tmp);
				try {
					value = Inet4Address.getByAddress(tmp);
				} catch(UnknownHostException e) {
					logger.warn("cannot handle IP address for type {} field", e);
				}
			}
			break;
		case SRC_MASK:
		case DST_MASK:
		case IPV6_DST_MASK:
		case IPV6_SRC_MASK:
		case ENGINE_ID:
		case FLOW_SAMPLER_ID:
		case MIN_TTL:
		case MAX_TTL:
		case MPLS_PREFIX_LEN:
		case FLOW_CLASS:
			value = new Integer(buffer.readUnsignedByte());				
			break;
		case SRC_AS:
		case DST_AS: 
			if(length == 2)
				value = new Integer(buffer.readUnsignedShort());
			else if(length == 4)
				value = new Long(buffer.readUnsignedInt());
			else {
				buffer.skipBytes(length);
				logger.warn("recieved type {} field with unsupported length {}", type, length);
			}
			break;
		case LAST_SWITCHED:
		case FIRST_SWITCHED:
		case SAMPLING_INTERVAL:
		case FLOW_SAMPLER_RANDOM_INTERVAL:
		case SRC_TRAFFIC_INDEX:
		case DST_TRAFFIC_INDEX:
		case REPLICATION_FACTOR:
			value = new Long(buffer.readUnsignedInt());
			break;
		case IPV6_SRC_ADDR:
		case IPV6_DST_ADDR: 
		case IPV6_NEXT_HOP:
		case BGP_IPV6_NEXT_HOP: {
				byte[] tmp = new byte[16];
				
				buffer.readBytes(tmp);
				try {
					value = Inet6Address.getByAddress(tmp);
				} catch(UnknownHostException e) {
					logger.warn("cannot handle IP address for type {} field", e);
				}
			}
			break;
		case IPV6_FLOW_LABEL:
		case MPLS_LABEL_1:
		case MPLS_LABEL_2:
		case MPLS_LABEL_3:
		case MPLS_LABEL_4:
		case MPLS_LABEL_5:
		case MPLS_LABEL_6:
		case MPLS_LABEL_7:
		case MPLS_LABEL_8:
		case MPLS_LABEL_9:
		case MPLS_LABEL_10:
			value = new Integer(buffer.readUnsignedMedium());
			break;
		case ICMP_TYPE:
			value = ICMPTypeCode.fromCodes(buffer.readUnsignedByte(), buffer.readUnsignedByte());
			break;
		case MUL_IGMP_TYPE: 
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<IGMPType>(IGMPType.fromCode(code), code);
			}
			break;
		case SAMPLING_ALGORITHM:
		case FLOW_SAMPLER_MODE:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<SamplingAlgorithm>(SamplingAlgorithm.fromCode(code), code);
			}
			break;
		case ENGINE_TYPE:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<EngineType>(EngineType.fromCode(code), code);
			}
			break;
		case MPLS_TOP_LABEL_TYPE:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<MPLSTopLabelType>(MPLSTopLabelType.fromCode(code), code);
			}
			break;
		case IN_DST_MAC:
		case OUT_SRC_MAC:
		case SRC_MAC:
		case DST_MAC:
			{
				byte[] mac = new byte[6];
				
				buffer.readBytes(mac);
				
				value = mac;
			}
			break;
		case IP_PROTOCOL_VERSION:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<IPProtocolVersion>(IPProtocolVersion.fromCode(code), code);
			}
			break;
		case DIRECTION:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<FlowDirection>(FlowDirection.fromCode(code), code);
			}
			break;
		case IPV6_OPTION_HEADERS:
			value = IPv6OptionHeaders.fromCode((int)buffer.readUnsignedInt());
			break;
		case IF_NAME:
		case IF_DESC:
		case SAMPLER_NAME:
		case APPLICATION_DESCRIPTION:
		case APPLICATION_NAME:
			{
				byte[] tmp = new byte[length];
				
				buffer.readBytes(tmp);
				
				try {
					value = new String(tmp, Charset.forName("UTF-8"));
				} catch(Exception e) {
					logger.warn("Cannot string of length {} for type {}", length, type, e);
				}
			}
			break;
		case FORWARDING_STATUS:
			{
				int code = buffer.readUnsignedByte();
				
				value = new EnumCodeValue<ForwardingStatus>(ForwardingStatus.fromCode(code), code);
			}
			break;
		case MPLS_PAL_RD:
		case APPLICATION_TAG:
		case DEPRECATED:
		case EXTENSION:
		case PROPRIETARY:
		case L2_PKT_SECT_DATA:
			{
				byte[] data = new byte[length];
				
				buffer.readBytes(data);
				
				value = new EncodedData(data, Base64.getEncoder().encodeToString(data));
			}
			break;
		}
		
		return value;
	}

	/**
	 * @param peerAddressMapper the peerAddressMapper to set
	 */
	public void setPeerAddressMapper(PeerAddressMapper peerAddressMapper) {
		this.peerAddressMapper = peerAddressMapper;
	}
}
