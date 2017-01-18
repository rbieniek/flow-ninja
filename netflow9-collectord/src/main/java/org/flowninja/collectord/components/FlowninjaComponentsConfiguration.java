package org.flowninja.collectord.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.flowninja.collector.common.netflow9.components.PortableDataFlowConverter;
import org.flowninja.collector.common.netflow9.components.PortableOptionsFlowConverter;
import org.flowninja.collectord.actors.FlowFileWriterActor;
import org.flowninja.common.akka.SpringActorProducer;

@Configuration
@ComponentScan(basePackageClasses = FlowninjaComponentsConfiguration.class)
public class FlowninjaComponentsConfiguration {

    @Profile("disk")
    @Configuration
    @EnableConfigurationProperties(FileSinkProperties.class)
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

        @Bean
        @Autowired
        public TargetDistributionActorProvider fileTargetActorProvider(final SpringActorProducer springActorProducer) {
            return () -> springActorProducer.createActor(FlowFileWriterActor.class);
        }
    }

}
