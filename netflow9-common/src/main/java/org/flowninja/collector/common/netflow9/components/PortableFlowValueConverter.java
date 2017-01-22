package org.flowninja.collector.common.netflow9.components;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.collector.common.types.EncodedData;
import org.flowninja.common.protocol.types.ForwardingStatus;
import org.flowninja.common.protocol.types.ICMPTypeCode;
import org.flowninja.common.protocol.types.IGMPType;
import org.flowninja.common.protocol.types.IPProtocol;
import org.flowninja.common.protocol.types.IPProtocolVersion;
import org.flowninja.common.protocol.types.IPTypeOfService;
import org.flowninja.common.protocol.types.IPv6OptionHeaders;
import org.flowninja.common.protocol.types.TCPFLags;
import org.flowninja.common.types.EngineType;
import org.flowninja.common.types.FlowDirection;
import org.flowninja.common.types.FlowStatistics;
import org.flowninja.common.types.InternetAddress;
import org.flowninja.common.types.InternetAddressType;
import org.flowninja.common.types.PayloadCounters;
import org.flowninja.common.types.PortableFlowValueRecord;
import org.flowninja.common.types.ProtocolFlags;
import org.flowninja.common.types.SamplingAlgorithm;
import org.flowninja.common.types.TypeOfService;

@Component
public class PortableFlowValueConverter {

    @SuppressWarnings({ "unchecked", "checkstyle:JavaNCSS", "checkstyle:CyclomaticComplexity" })
    public PortableFlowValueRecord convertFlowValueRecord(final List<FlowValueRecord> records) {
        final PortableFlowValueRecord flow = new PortableFlowValueRecord();

        for (final FlowValueRecord record : records) {
            switch (record.getType()) {
            case IPV4_SRC_ADDR:
            case IPV6_SRC_ADDR:
            case IPV4_SRC_PREFIX:
                putInternetAddress(
                        (InetAddress) record.getValue(),
                        () -> onDemandField(
                                flow,
                                f -> f.getSrcAddress(),
                                (g, a) -> g.setSrcAddress(a),
                                () -> new InternetAddress()));
                break;
            case IPV4_DST_ADDR:
            case IPV6_DST_ADDR:
            case IPV4_DST_PREFIX:
                putInternetAddress(
                        (InetAddress) record.getValue(),
                        () -> onDemandField(
                                flow,
                                f -> f.getDstAddress(),
                                (g, a) -> g.setDstAddress(a),
                                () -> new InternetAddress()));
                break;
            case IPV4_NEXT_HOP:
            case IPV6_NEXT_HOP:
                putInternetAddress(
                        (InetAddress) record.getValue(),
                        () -> onDemandField(flow, f -> f.getNextHop(), (g, a) -> g.setNextHop(a), () -> new InternetAddress()));
                break;
            case BGP_IPV4_NEXT_HOP:
            case BGP_IPV6_NEXT_HOP:
                putInternetAddress(
                        (InetAddress) record.getValue(),
                        () -> onDemandField(
                                flow,
                                f -> f.getBgpNextHop(),
                                (g, a) -> g.setBgpNextHop(a),
                                () -> new InternetAddress()));
                break;
            case SRC_MASK:
            case IPV6_SRC_MASK:
                onDemandField(flow, f -> f.getSrcAddress(), (g, a) -> g.setSrcAddress(a), () -> new InternetAddress())
                        .setMask((Integer) record.getValue());
                break;
            case DST_MASK:
            case IPV6_DST_MASK:
                onDemandField(flow, f -> f.getDstAddress(), (g, a) -> g.setDstAddress(a), () -> new InternetAddress())
                        .setMask((Integer) record.getValue());
                break;
            case L4_DST_PORT:
                onDemandField(flow, f -> f.getDstAddress(), (g, a) -> g.setDstAddress(a), () -> new InternetAddress())
                        .setPort((Integer) record.getValue());
                break;
            case L4_SRC_PORT:
                onDemandField(flow, f -> f.getSrcAddress(), (g, a) -> g.setSrcAddress(a), () -> new InternetAddress())
                        .setPort((Integer) record.getValue());
                break;
            case DST_AS:
                onDemandField(flow, f -> f.getDstAddress(), (g, a) -> g.setDstAddress(a), () -> new InternetAddress())
                        .setAsNumber((Integer) record.getValue());
                break;
            case SRC_AS:
                onDemandField(flow, f -> f.getSrcAddress(), (g, a) -> g.setSrcAddress(a), () -> new InternetAddress())
                        .setAsNumber((Integer) record.getValue());
                break;
            case DST_VLAN:
                onDemandField(flow, f -> f.getDstAddress(), (g, a) -> g.setDstAddress(a), () -> new InternetAddress())
                        .setVlanNumber((Integer) record.getValue());
                break;
            case SRC_VLAN:
                onDemandField(flow, f -> f.getSrcAddress(), (g, a) -> g.setSrcAddress(a), () -> new InternetAddress())
                        .setVlanNumber((Integer) record.getValue());
                break;
            case DST_TRAFFIC_INDEX:
                onDemandField(flow, f -> f.getDstAddress(), (g, a) -> g.setDstAddress(a), () -> new InternetAddress())
                        .setTrafficIndex((Integer) record.getValue());
                break;
            case SRC_TRAFFIC_INDEX:
                onDemandField(flow, f -> f.getSrcAddress(), (g, a) -> g.setSrcAddress(a), () -> new InternetAddress())
                        .setTrafficIndex((Integer) record.getValue());
                break;
            case INPUT_SNMP:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getSrcAddress(),
                                (g, a) -> g.setSrcAddress(a),
                                () -> new InternetAddress()).setSnmpInterfaceIndex(bi));
                break;
            case OUTPUT_SNMP:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getDstAddress(),
                                (g, a) -> g.setDstAddress(a),
                                () -> new InternetAddress()).setSnmpInterfaceIndex(bi));
                break;
            case IN_BYTES:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setInBytes(bi));
                break;
            case IN_PKTS:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setInPkts(bi));
                break;
            case MUL_DST_BYTES:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setMulDstBytes(bi));
                break;
            case MUL_DST_PKTS:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setMulDstPkts(bi));
                break;
            case OUT_BYTES:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setOutBytes(bi));
                break;
            case OUT_PKTS:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setOutPkts(bi));
                break;
            case TOTAL_BYTES_EXP:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setTotalBytesExp(bi));
                break;
            case TOTAL_PKTS_EXP:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setTotalPktsExp(bi));
                break;
            case IN_PERMANENT_BYTES:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setInPermanentBytes(bi));
                break;
            case IN_PERMANENT_PKTS:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getPayloadCounters(),
                                (g, v) -> g.setPayloadCounters(v),
                                () -> new PayloadCounters()).setInPermanentBytes(bi));
                break;
            case SRC_TOS:
                onDemandField(flow, f -> f.getTypeOfService(), (g, v) -> g.setTypeOfService(v), () -> new TypeOfService())
                        .setSrcTos((IPTypeOfService) record.getValue());
                break;
            case DST_TOS:
                onDemandField(flow, f -> f.getTypeOfService(), (g, v) -> g.setTypeOfService(v), () -> new TypeOfService())
                        .setDstTos((IPTypeOfService) record.getValue());
                break;
            case POST_IP_DIFF_SERV_CODE_POINT:
                onDemandField(flow, f -> f.getTypeOfService(), (g, v) -> g.setTypeOfService(v), () -> new TypeOfService())
                        .setPostIpDiffServCodePoint((IPTypeOfService) record.getValue());
                break;
            case MIN_PKT_LNGTH:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setMinPktLength((Integer) record.getValue());
                break;
            case MAX_PKT_LNGTH:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setMaxPktLength((Integer) record.getValue());
                break;
            case FLOW_ACTIVE_TIMEOUT:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowActiveTimeout((Integer) record.getValue());
                break;
            case FLOW_INACTIVE_TIMEOUT:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowInactiveTimeout((Integer) record.getValue());
                break;
            case TOTAL_FLOWS_EXP:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setTotalFlowsExported((Integer) record.getValue());
                break;
            case FLOW_CLASS:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowClass((Integer) record.getValue());
                break;
            case SAMPLING_INTERVAL:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setSamplingInterval((Integer) record.getValue());
                break;
            case FLOW_SAMPLER_RANDOM_INTERVAL:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowSamplerRandomInterval((Long) record.getValue());
                break;
            case LAST_SWITCHED:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setLastSwitched((Long) record.getValue());
                break;
            case FIRST_SWITCHED:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFirstSwitched((Long) record.getValue());
                break;
            case DIRECTION:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowDirection((FlowDirection) record.getValue());
                break;
            case APPLICATION_DESCRIPTION:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setApplicationDescription((String) record.getValue());
                break;
            case APPLICATION_NAME:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setApplicationName((String) record.getValue());
                break;
            case APPLICATION_TAG:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setApplicationDescription(((EncodedData) record.getValue()).getEncodedData());
                break;
            case ENGINE_ID:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setEngineId((Integer) record.getValue());
                break;
            case ENGINE_TYPE:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setEngineType((EngineType) record.getValue());
                break;
            case FLOW_SAMPLER_ID:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setFlowSamplerId((Integer) record.getValue());
                break;
            case FLOW_SAMPLER_MODE:
            case SAMPLING_ALGORITHM:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setSamplingAlgorithm((SamplingAlgorithm) record.getValue());
                break;
            case FLOWS:
                putCounter(
                        (Counter) record.getValue(),
                        bi -> onDemandField(
                                flow,
                                f -> f.getFlowStatistics(),
                                (g, v) -> g.setFlowStatistics(v),
                                () -> new FlowStatistics()).setFlows(bi));
                break;
            case SAMPLER_NAME:
                onDemandField(flow, f -> f.getFlowStatistics(), (g, v) -> g.setFlowStatistics(v), () -> new FlowStatistics())
                        .setSamplerName((String) record.getValue());
                break;
            case PROTOCOL:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIpProtocol((IPProtocol) record.getValue());
                break;
            case IP_PROTOCOL_VERSION:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIpProtocolVersion((IPProtocolVersion) record.getValue());
                break;
            case ICMP_TYPE:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIcmpCode(((ICMPTypeCode) record.getValue()).getCode());
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIcmpType(((ICMPTypeCode) record.getValue()).getType());
                break;
            case MUL_IGMP_TYPE:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIgmpType((IGMPType) record.getValue());
                break;
            case FORWARDING_STATUS:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setForwardingStatus((ForwardingStatus) record.getValue());
                break;
            case IPV6_OPTION_HEADERS:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIpv6OptionHeaders(setToList((Set<IPv6OptionHeaders>) record.getValue()));
                break;
            case TCP_FLAGS:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setTcpFlags(setToList((Set<TCPFLags>) record.getValue()));
                break;
            case MAX_TTL:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setMinTtl((Integer) record.getValue());
                break;
            case MIN_TTL:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setMaxTtl((Integer) record.getValue());
                break;
            case IPV4_IDENT:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIpv4Ident((Integer) record.getValue());
                break;
            case IPV6_FLOW_LABEL:
                onDemandField(flow, f -> f.getProtocolFlags(), (g, v) -> g.setProtocolFlags(v), () -> new ProtocolFlags())
                        .setIpv6FlowLabel((Integer) record.getValue());
                break;

            case IF_DESC:
                break;
            case IF_NAME:
                break;
            default:
                break;
            }
        }

        return flow;
    }

    private void putInternetAddress(final InetAddress addr, final Supplier<InternetAddress> supplier) {
        final InternetAddress internetAddress = supplier.get();

        internetAddress.setBase64Rep(Base64.getEncoder().encodeToString(addr.getAddress()));
        internetAddress.setTextRep(addr.getHostAddress());
        internetAddress.setType(addr instanceof Inet6Address ? InternetAddressType.IPV6 : InternetAddressType.IPV4);
    }

    private <T> T onDemandField(
            final PortableFlowValueRecord flow,
            final Function<PortableFlowValueRecord, T> getter,
            final BiConsumer<PortableFlowValueRecord, T> setter,
            final Supplier<T> producer) {
        final T exValue = getter.apply(flow);
        if (exValue == null) {
            final T value = producer.get();

            setter.accept(flow, value);
            return value;
        }

        return exValue;
    }

    private void putNumber(final Number value, final Consumer<BigInteger> consumer) {
        if (value instanceof BigInteger) {
            consumer.accept((BigInteger) value);
        } else if (value instanceof Long) {
            consumer.accept(BigInteger.valueOf((Long) value));
        } else if (value instanceof Integer) {
            consumer.accept(BigInteger.valueOf(Integer.valueOf((Integer) value).longValue()));
        } else {
            consumer.accept(BigInteger.ZERO);
        }
    }

    private void putCounter(final Counter counter, final Consumer<BigInteger> consumer) {
        final Number value = counter.value();

        if (value instanceof BigInteger) {
            consumer.accept((BigInteger) value);
        } else if (value instanceof Long) {
            consumer.accept(BigInteger.valueOf((Long) value));
        } else if (value instanceof Integer) {
            consumer.accept(BigInteger.valueOf(Integer.valueOf((Integer) value).longValue()));
        } else {
            consumer.accept(BigInteger.ZERO);
        }
    }

    private <T> List<T> setToList(final Set<T> set) {
        return set.stream().collect(Collectors.toList());
    }

}
