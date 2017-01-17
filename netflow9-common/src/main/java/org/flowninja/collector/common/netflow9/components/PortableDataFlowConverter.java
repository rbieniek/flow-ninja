package org.flowninja.collector.common.netflow9.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.PortableDataFlow;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class PortableDataFlowConverter {

    private final PortableFlowValueConverter portableFlowValueConverter;

    public PortableDataFlow convertDataFlow(final DataFlow dataFlow) {
        return PortableDataFlow.builder()
                .header(dataFlow.getHeader())
                .peerAddress(dataFlow.getPeerAddress().getHostAddress())
                .uuid(dataFlow.getUuid().toString())
                .records(portableFlowValueConverter.convertFlowValueRecords(dataFlow.getRecords()))
                .build();
    }
}
