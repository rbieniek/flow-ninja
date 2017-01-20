package org.flowninja.collector.common.netflow9.components;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Base64;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.common.types.InternetAddress;
import org.flowninja.common.types.InternetAddressType;
import org.flowninja.common.types.PayloadCounters;
import org.flowninja.common.types.PortableDataFlow;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class PortableDataFlowConverter {

    private final PortableFlowValueConverter portableFlowValueConverter;

    public PortableDataFlow convertDataFlow(final DataFlow dataFlow) {
        final PortableDataFlow portableFlow = PortableDataFlow.builder()
                .header(dataFlow.getHeader())
                .peerAddress(dataFlow.getPeerAddress().getHostAddress())
                .uuid(dataFlow.getUuid().toString())
                .records(portableFlowValueConverter.convertFlowValueRecords(dataFlow.getRecords()))
                .build();

        fillPortableFlowFromFlowRecords(portableFlow, dataFlow.getRecords());

        return portableFlow;
    }

    private void fillPortableFlowFromFlowRecords(final PortableDataFlow flow, final List<FlowValueRecord> records) {
        for (final FlowValueRecord record : records) {
            switch (record.getType()) {
            case IPV4_SRC_ADDR:
            case IPV6_SRC_ADDR:
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
            case TOTAL_FLOWS_EXP:
                break;
            case INPUT_SNMP:
                break;
            case OUTPUT_SNMP:
                break;
            case L2_PKT_SECT_OFFSET:
                break;
            case L2_PKT_SECT_SIZE:
                break;
            case FLOWS:
                break;
            }
        }
    }

    private void putInternetAddress(final InetAddress addr, final Supplier<InternetAddress> supplier) {
        final InternetAddress internetAddress = supplier.get();

        internetAddress.setBase64Rep(Base64.getEncoder().encodeToString(addr.getAddress()));
        internetAddress.setTextRep(addr.getHostAddress());
        internetAddress.setType(addr instanceof Inet6Address ? InternetAddressType.IPV6 : InternetAddressType.IPV4);
    }

    private <T> T onDemandField(
            final PortableDataFlow flow,
            final Function<PortableDataFlow, T> getter,
            final BiConsumer<PortableDataFlow, T> setter,
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
}
