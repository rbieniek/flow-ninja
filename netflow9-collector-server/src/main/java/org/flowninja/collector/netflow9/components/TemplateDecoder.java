package org.flowninja.collector.netflow9.components;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataFlowRecord;
import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.DataTemplateField;
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
     *
     * @param peerAddress
     *
     * @param header
     * @param flowBuffer
     * @param template
     * @return
     */
    public List<DataFlow> decodeDataTemplate(
            final InetAddress peerAddress,
            final FlowBuffer flowBuffer,
            final DataTemplate template) {
        final ByteBuf buffer = flowBuffer.getBuffer();
        final List<DataFlow> flows = new LinkedList<>();
        final int dataLength = template.getTemplateLength();

        buffer.markReaderIndex();

        while (buffer.readableBytes() >= dataLength) {
            final List<DataFlowRecord> flowRecords = new LinkedList<>();

            for (final DataTemplateField field : template.getFields()) {
                flowRecords.add(
                        DataFlowRecord.builder()
                                .type(field.getType())
                                .value(decodeValue(field.getType(), field.getLength(), buffer))
                                .build());
            }

            flows.add(
                    DataFlow.builder()
                            .peerAddress(peerAddress)
                            .header(flowBuffer.getHeader())
                            .records(flowRecords)
                            .uuid(UUID.randomUUID())
                            .build());
        }

        return flows;
    }

    /**
     * Decode a data flow from a data buffer and a given template
     *
     * @param peerAddress
     *
     * @param header
     * @param flowBuffer
     * @param template
     * @return
     */
    public List<OptionsFlow> decodeOptionsTemplate(
            final InetAddress peerAddress,
            final FlowBuffer flowBuffer,
            final OptionsTemplate template) {
        final ByteBuf buffer = flowBuffer.getBuffer();
        final int dataLength = template.getTemplateLength();
        final List<OptionsFlow> optionsFlows = new LinkedList<>();

        buffer.markReaderIndex();

        while (buffer.readableBytes() >= dataLength) {
            final List<OptionsFlowRecord> flowRecords = new LinkedList<>();
            final List<ScopeFlowRecord> scopeRecords = new LinkedList<>();

            for (final ScopeField field : template.getScopeFields()) {
                Counter value = null;

                if (field.getLength() > 0) {
                    final byte[] data = new byte[field.getLength()];

                    buffer.readBytes(data);

                    value = CounterFactory.decode(data);
                }

                scopeRecords.add(ScopeFlowRecord.builder().type(field.getType()).value(value).build());
            }

            for (final OptionField field : template.getOptionFields()) {
                flowRecords.add(
                        OptionsFlowRecord.builder()
                                .type(field.getType())
                                .value(decodeValue(field.getType(), field.getLength(), buffer))
                                .build());
            }

            optionsFlows.add(
                    OptionsFlow.builder()
                            .peerAddress(peerAddress)
                            .header(flowBuffer.getHeader())
                            .scopes(scopeRecords)
                            .records(flowRecords)
                            .uuid(UUID.randomUUID())
                            .build());
        }

        return optionsFlows;
    }

    /**
     *
     * @param type
     * @param buffer
     * @return
     */
    @SuppressWarnings({ "checkstyle:JavaNCSS", "checkstyle:CyclomaticComplexity", "checkstyle:IllegalInstantiation" })
    private Object decodeValue(final FieldType type, final int length, final ByteBuf buffer) {
        Object value = null;

        try {
            switch (type) {
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
                value = CounterFactory.decode(readBytes(length, buffer));
                break;
            case PROTOCOL:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> IPProtocol.fromCode(c));
                break;
            case SRC_TOS:
            case DST_TOS:
            case POST_IP_DIFF_SERV_CODE_POINT:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> IPTypeOfService.fromCode(c));
                break;
            case TCP_FLAGS:
                if (length == 1) {
                    value = TCPFLags.fromCode(buffer.readUnsignedByte());
                } else if (length == 2) {
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
                value = Integer.valueOf(buffer.readUnsignedShort());
                break;
            case IPV4_SRC_ADDR:
            case IPV4_DST_ADDR:
            case IPV4_DST_PREFIX:
            case IPV4_SRC_PREFIX:
            case IPV4_NEXT_HOP:
            case MPLS_TOP_LABEL_IP_ADDR:
            case BGP_IPV4_NEXT_HOP:
                value = Inet4Address.getByAddress(readBytes(4, buffer));
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
                value = Integer.valueOf(buffer.readUnsignedByte());
                break;
            case SRC_AS:
            case DST_AS:
                if (length == 2) {
                    value = Integer.valueOf(buffer.readUnsignedShort());
                } else if (length == 4) {
                    value = Long.valueOf(buffer.readUnsignedInt());
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
                value = Long.valueOf(buffer.readUnsignedInt());
                break;
            case IPV6_SRC_ADDR:
            case IPV6_DST_ADDR:
            case IPV6_NEXT_HOP:
            case BGP_IPV6_NEXT_HOP:
                value = Inet6Address.getByAddress(readBytes(16, buffer));
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
                value = Integer.valueOf(buffer.readUnsignedMedium());
                break;
            case ICMP_TYPE:
                value = ICMPTypeCode.fromCodes(buffer.readUnsignedByte(), buffer.readUnsignedByte());
                break;
            case MUL_IGMP_TYPE:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> IGMPType.fromCode(c));
                break;
            case SAMPLING_ALGORITHM:
            case FLOW_SAMPLER_MODE:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> SamplingAlgorithm.fromCode(c));
                break;
            case ENGINE_TYPE:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> EngineType.fromCode(c));
                break;
            case MPLS_TOP_LABEL_TYPE:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> MPLSTopLabelType.fromCode(c));
                break;
            case IN_DST_MAC:
            case OUT_SRC_MAC:
            case SRC_MAC:
            case DST_MAC:
                value = readBytes(6, buffer);
                break;
            case IP_PROTOCOL_VERSION:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> IPProtocolVersion.fromCode(c));
                break;
            case DIRECTION:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> FlowDirection.fromCode(c));
                break;
            case IPV6_OPTION_HEADERS:
                value = IPv6OptionHeaders.fromCode((int) buffer.readUnsignedInt());
                break;
            case IF_NAME:
            case IF_DESC:
            case SAMPLER_NAME:
            case APPLICATION_DESCRIPTION:
            case APPLICATION_NAME:
                value = new String(readBytes(length, buffer), Charset.forName("UTF-8"));
                break;
            case FORWARDING_STATUS:
                value = readEnumCodeValue(() -> (int) buffer.readUnsignedByte(), c -> ForwardingStatus.fromCode(c));
                break;
            case MPLS_PAL_RD:
            case APPLICATION_TAG:
            case DEPRECATED:
            case EXTENSION:
            case PROPRIETARY:
            case L2_PKT_SECT_DATA:
                value = readEncodedData(length, buffer);
                break;
            default:
                log.info("Unknown type code {}", type);
                break;
            }
        } catch (final UnknownHostException e) {
            log.warn("cannot handle IP address for type {} field", e);
        } catch (final IllegalArgumentException e) {
            log.warn("Cannot create string of length {} for type {}", length, type, e);
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> EnumCodeValue<T> readEnumCodeValue(
            final Supplier<Integer> codeSupplier,
            final Function<Integer, T> valueSupplier) {
        final int code = codeSupplier.get();

        return (EnumCodeValue<T>) EnumCodeValue.builder().value(valueSupplier.apply(code)).code(code).build();
    }

    private EncodedData readEncodedData(final int length, final ByteBuf buffer) {
        final byte[] data = readBytes(length, buffer);

        return EncodedData.builder().data(data).encodedData(Base64.getEncoder().encodeToString(data)).build();
    }

    private byte[] readBytes(final int length, final ByteBuf buffer) {
        final byte[] data = new byte[length];

        buffer.readBytes(data);

        return data;
    }
}
