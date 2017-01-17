package org.flowninja.collector.common.netflow9.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.netflow9.types.PortableFlowValueRecord;

@Component
public class PortableFlowValueConverter {

    public List<PortableFlowValueRecord> convertFlowValueRecords(final List<FlowValueRecord> records) {
        return records.stream().map(r -> convertFlowValueRecord(r)).collect(Collectors.toList());
    }

    public PortableFlowValueRecord convertFlowValueRecord(final FlowValueRecord record) {
        final PortableFlowValueRecord pfr = new PortableFlowValueRecord();

        pfr.setType(record.getType().toString());

        return pfr;
    }

}
