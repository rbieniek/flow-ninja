package org.flowninja.collector.netflow9.components;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataFlowRecord;
import org.flowninja.collector.common.netflow9.types.EngineType;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowDirection;
import org.flowninja.collector.common.netflow9.types.IPv6OptionHeaders;
import org.flowninja.collector.common.netflow9.types.OptionField;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlowRecord;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.SamplingAlgorithm;
import org.flowninja.collector.common.netflow9.types.ScopeField;
import org.flowninja.collector.common.netflow9.types.ScopeFlowRecord;
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
import org.flowninja.collector.netflow9.packet.FlowBuffer;

import io.netty.buffer.ByteBuf;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TemplateDecoder {

    /**
     * Decode a data flow from a data buffer and a given template
     * @param peerAddress
     *
     * @param header
     * @param flowBuffer
     * @param template
     * @return
     */
    public List<DataFlow> decodeDataTemplate(final InetAddress peerAddress, final FlowBuffer flowBuffer, final Template template) {
        final ByteBuf buffer = flowBuffer.getBuffer();
        final List<DataFlow> flows = new LinkedList<>();
        final int dataLength = template.getTemplateLength();

        while(buffer.readableBytes() >= dataLength) {
            final List<DataFlowRecord> flowRecords = new LinkedList<>();

            for(final TemplateField field : template.getFields()) {
                flowRecords.add(new DataFlowRecord(field.getType(), decodeValue(field.getType(), field.getLength(), buffer)));
            }

            flows.add(new DataFlow(peerAddress, flowBuffer.getHeader(), flowRecords));
        }

        return flows;
    }

    /**
     * Decode a data flow from a data buffer and a given template
     * @param peerAddress
     *
     * @param header
     * @param flowBuffer
     * @param template
     * @return
     */
    public List<OptionsFlow> decodeOptionsTemplate(final InetAddress peerAddress, final FlowBuffer flowBuffer, final OptionsTemplate template) {
        final ByteBuf buffer = flowBuffer.getBuffer();
        final int dataLength = template.getTemplateLength();
        final List<OptionsFlow> optionsFlows = new LinkedList<>();

        while(buffer.readableBytes() >= dataLength) {
            final List<OptionsFlowRecord> flowRecords = new LinkedList<>();
            final List<ScopeFlowRecord> scopeRecords = new LinkedList<>();

            for(final ScopeField field : template.getScopeFields()) {
                Counter value = null;

                if(field.getLength() > 0) {
                    final byte data[] = new byte[field.getLength()];

                    buffer.readBytes(data);

                    value = CounterFactory.decode(data);
                }

                scopeRecords.add(new ScopeFlowRecord(field.getType(), value));
            }

            for(final OptionField field : template.getOptionFields()) {
                flowRecords.add(new OptionsFlowRecord(field.getType(), decodeValue(field.getType(), field.getLength(), buffer)));
            }

            optionsFlows.add(new OptionsFlow(peerAddress, flowBuffer.getHeader(), scopeRecords, flowRecords));
        }

        return optionsFlows;
    }

    /**
     *
     * @param type
     * @param buffer
     * @return
     */
    private Object decodeValue(final FieldType type, final int length, final ByteBuf buffer) {
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
            final byte[] dst = new byte[length];

            buffer.readBytes(dst);
            value = CounterFactory.decode(dst);
        }
        break;
        case PROTOCOL:
        {
            final int code = buffer.readUnsignedByte();
            value = new EnumCodeValue<>(IPProtocol.fromCore(code), code);
        }
        break;
        case SRC_TOS:
        case DST_TOS:
        case POST_IP_DIFF_SERV_CODE_POINT:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(IPTypeOfService.fromCode(code), code);
        }
        break;
        case TCP_FLAGS:
            if(length == 1) {
                value = TCPFLags.fromCode(buffer.readUnsignedByte());
            } else if(length == 2) {
                value = TCPFLags.fromCode(buffer.readUnsignedShort());
            } else {
                buffer.skipBytes(length);
                log.warn("recieved type {} field with unsupported length {}", type, length);
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
            final byte[] tmp = new byte[4];

            buffer.readBytes(tmp);
            try {
                value = Inet4Address.getByAddress(tmp);
            } catch(final UnknownHostException e) {
                log.warn("cannot handle IP address for type {} field", e);
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
            if(length == 2) {
                value = new Integer(buffer.readUnsignedShort());
            } else if(length == 4) {
                value = new Long(buffer.readUnsignedInt());
            } else {
                buffer.skipBytes(length);
                log.warn("recieved type {} field with unsupported length {}", type, length);
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
            final byte[] tmp = new byte[16];

            buffer.readBytes(tmp);
            try {
                value = Inet6Address.getByAddress(tmp);
            } catch(final UnknownHostException e) {
                log.warn("cannot handle IP address for type {} field", e);
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
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(IGMPType.fromCode(code), code);
        }
        break;
        case SAMPLING_ALGORITHM:
        case FLOW_SAMPLER_MODE:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(SamplingAlgorithm.fromCode(code), code);
        }
        break;
        case ENGINE_TYPE:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(EngineType.fromCode(code), code);
        }
        break;
        case MPLS_TOP_LABEL_TYPE:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(MPLSTopLabelType.fromCode(code), code);
        }
        break;
        case IN_DST_MAC:
        case OUT_SRC_MAC:
        case SRC_MAC:
        case DST_MAC:
        {
            final byte[] mac = new byte[6];

            buffer.readBytes(mac);

            value = mac;
        }
        break;
        case IP_PROTOCOL_VERSION:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(IPProtocolVersion.fromCode(code), code);
        }
        break;
        case DIRECTION:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(FlowDirection.fromCode(code), code);
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
            final byte[] tmp = new byte[length];

            buffer.readBytes(tmp);

            try {
                value = new String(tmp, Charset.forName("UTF-8"));
            } catch(final Exception e) {
                log.warn("Cannot string of length {} for type {}", length, type, e);
            }
        }
        break;
        case FORWARDING_STATUS:
        {
            final int code = buffer.readUnsignedByte();

            value = new EnumCodeValue<>(ForwardingStatus.fromCode(code), code);
        }
        break;
        case MPLS_PAL_RD:
        case APPLICATION_TAG:
        case DEPRECATED:
        case EXTENSION:
        case PROPRIETARY:
        case L2_PKT_SECT_DATA:
        {
            final byte[] data = new byte[length];

            buffer.readBytes(data);

            value = new EncodedData(data, Base64.getEncoder().encodeToString(data));
        }
        break;
        }

        return value;
    }

}
