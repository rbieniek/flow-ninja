package org.flowninja.collectord.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.flowninja.collector.common.netflow9.components.PortableDataFlowConverter;
import org.flowninja.collector.common.netflow9.components.PortableOptionsFlowConverter;

@Configuration
@ComponentScan(basePackageClasses = FlowninjaComponentsConfiguration.class)
public class FlowninjaComponentsConfiguration {

    @ConditionalOnProperty(name = "collectord.disk.enabled")
    @Configuration
    public static class DiskSinkConfiguration {

        @Bean
        @Autowired
        public FileSinkManager fileSinkManager(final FileSinkProperties fileSinkProperties) {
            return new FileSinkManager(fileSinkProperties);
        }

        @Bean
        @Autowired
        public FlowFileWriter flowFileWriter(
                final FileSinkManager fileSinkManager,
                final PortableDataFlowConverter portableDataFlowConverter,
                final PortableOptionsFlowConverter portableOptionsFlowConverter) {
            return new FlowFileWriter(fileSinkManager, portableDataFlowConverter, portableOptionsFlowConverter);
        }
    }

}
