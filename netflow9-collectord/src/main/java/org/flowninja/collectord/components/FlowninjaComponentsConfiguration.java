package org.flowninja.collectord.components;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = FlowninjaComponentsConfiguration.class)
public class FlowninjaComponentsConfiguration {

    @ConditionalOnProperty(name = "collectord.disk.enabled")
    @Configuration
    public static class DiskSinkConfiguration {

    }

}
