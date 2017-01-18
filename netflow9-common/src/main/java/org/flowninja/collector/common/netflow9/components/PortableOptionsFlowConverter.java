package org.flowninja.collector.common.netflow9.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.ScopeFlowRecord;
import org.flowninja.common.types.PortableOptionsFlow;
import org.flowninja.common.types.PortableScopeFlowRecord;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class PortableOptionsFlowConverter {

    private final PortableFlowValueConverter portableFlowValueConverter;

    public PortableOptionsFlow convertOptionsFlow(final OptionsFlow optionsFlow) {
        return PortableOptionsFlow.builder()
                .header(optionsFlow.getHeader())
                .peerAddress(optionsFlow.getPeerAddress().getHostAddress())
                .uuid(optionsFlow.getUuid().toString())
                .records(portableFlowValueConverter.convertFlowValueRecords(optionsFlow.getRecords()))
                .scopes(convertScopeFlowRecords(optionsFlow.getScopes()))
                .build();
    }

    private List<PortableScopeFlowRecord> convertScopeFlowRecords(final List<ScopeFlowRecord> records) {
        return records.stream().map(r -> convertScopeFlowRecord(r)).collect(Collectors.toList());
    }

    private PortableScopeFlowRecord convertScopeFlowRecord(final ScopeFlowRecord record) {
        final PortableScopeFlowRecord pfr = new PortableScopeFlowRecord();

        pfr.setType(record.getType().toString());

        return pfr;
    }
}
