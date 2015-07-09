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
	public DirectChannel sourceFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel sourceDataFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel sourceDataFlowChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel sourceOptionsFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel headerFileOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel unprocessableFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel headerFileInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel sourceOptionsFlowChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public DirectChannel correlatorInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel correlatorOutputChannel() {
		return new DirectChannel();
	}

}
