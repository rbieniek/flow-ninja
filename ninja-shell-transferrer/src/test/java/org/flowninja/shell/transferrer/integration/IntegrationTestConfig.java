/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

/**
 * @author rainer
 *
 */
@Configuration
@ComponentScan(basePackageClasses=IntegrationTestConfig.class)
@IntegrationComponentScan(basePackageClasses=IntegrationTestConfig.class)
@EnableIntegration
@PropertySource("classpath:/test-config.properties")
public class IntegrationTestConfig {
	@Bean
	public MessageChannel sourceFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceDataFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceDataFlowChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceOptionsFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceOptionsFlowChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel unprocessableFileChannel() {
		return new DirectChannel();
	}

}
