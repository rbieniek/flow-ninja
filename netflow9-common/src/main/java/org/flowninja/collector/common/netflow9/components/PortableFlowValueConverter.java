package org.flowninja.collector.common.netflow9.components;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.netflow9.types.PortableFlowValueRecord;
import org.flowninja.collector.common.types.CollectionSource;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.collector.common.types.EncodedData;

@Component
public class PortableFlowValueConverter {

    public List<PortableFlowValueRecord> convertFlowValueRecords(final List<FlowValueRecord> records) {
        return records.stream().map(r -> convertFlowValueRecord(r)).collect(Collectors.toList());
    }

    public PortableFlowValueRecord convertFlowValueRecord(final FlowValueRecord record) {
        final PortableFlowValueRecord pfr = new PortableFlowValueRecord();
        final Object value = record.getValue();

        if (record.getType() != null) {
            pfr.setType(record.getType().toString());
        }

        if (value.getClass().isArray()) {
            pfr.setObjectClass(Array.class.getName());
        } else {
            pfr.setObjectClass(value.getClass().getName());
        }

        if (value instanceof Counter) {
            pfr.setNumberValue(BigInteger.valueOf(((Counter) value).value().longValue()));
        } else if (value instanceof Enum<?>) {
            pfr.setEnumValue(value.toString());
        } else if (value instanceof Collection<?>) {
            pfr.setObjectClass(Collection.class.getName());
            pfr.setCollectionValue(
                    ((Collection<?>) value).stream()
                            .map(o -> convertFlowValueRecord(FlowValueRecord.builder().value(o).build()))
                            .collect(Collectors.toList()));
        } else if (value instanceof CollectionSource) {
            pfr.setObjectClass(Collection.class.getName());
            pfr.setCollectionValue(
                    ((CollectionSource) value).toCollection()
                            .stream()
                            .map(o -> convertFlowValueRecord(FlowValueRecord.builder().value(o).build()))
                            .collect(Collectors.toList()));
        } else if (value instanceof Number) {
            pfr.setNumberValue(BigInteger.valueOf(((Number) value).longValue()));
        } else if (value instanceof InetAddress) {
            pfr.setAddrValue(((InetAddress) value).getHostAddress());
        } else if (value instanceof byte[]) {
            pfr.setEncodedValue(Base64.getEncoder().encodeToString((byte[]) value));
        } else if (value instanceof EncodedData) {
            pfr.setEncodedValue(((EncodedData) value).getEncodedData());
        } else if (value instanceof String) {
            pfr.setStringValue((String) value);
        }

        return pfr;
    }

}
