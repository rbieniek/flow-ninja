package org.flowninja.collectord.components;

import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.InitializingBean;

import org.flowninja.collector.common.netflow9.components.PortableDataFlowConverter;
import org.flowninja.collector.common.netflow9.components.PortableOptionsFlowConverter;
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FlowFileWriter implements InitializingBean {

    private final FileSinkManager fileSinkManager;
    private final PortableDataFlowConverter portableDataFlowConverter;
    private final PortableOptionsFlowConverter portableOptionsFlowConverter;

    private ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.objectMapper = new ObjectMapper();
    }

    public void writeDataFlow(final DataFlow dataFlow) {
        try {
            final FileOutputStream outputStream = new FileOutputStream(
                    fileSinkManager.createTargetFile("data-" + dataFlow.getUuid().toString() + ".json"));

            objectMapper.writeValue(outputStream, portableDataFlowConverter.convertDataFlow(dataFlow));

            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            log.info("Failed to write data flow {}", dataFlow, e);

            throw new RuntimeException(e);
        }
    }

    public void writeOptionsFlow(final OptionsFlow optionsFlow) {
        try {
            final FileOutputStream outputStream = new FileOutputStream(
                    fileSinkManager.createTargetFile("options-" + optionsFlow.getUuid().toString() + ".json"));

            objectMapper.writeValue(outputStream, portableOptionsFlowConverter.convertOptionsFlow(optionsFlow));

            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            log.info("Failed to write data flow {}", optionsFlow, e);

            throw new RuntimeException(e);
        }
    }
}
