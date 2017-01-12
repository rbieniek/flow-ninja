package org.flowninja.common.kafka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=KafkaCommonConfiguration.class)
public class KafkaCommonConfiguration {

}
