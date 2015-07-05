/**
 * 
 */
package org.flowninja.shell.transferrer;

import java.io.File;

import org.flowninja.shell.transferrer.integration.IgnoreCurrentHourFileFilter;
import org.flowninja.shell.transferrer.integration.SetFileNameHeaderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.messaging.MessageChannel;

/**
 * @author rainer
 *
 */
@Configuration
@EnableIntegration
@ComponentScan(basePackageClasses=TransferrerConfig.class)
@IntegrationComponentScan(basePackageClasses=IgnoreCurrentHourFileFilter.class)
public class TransferrerConfig {
	@Value("#{systemProperties.sourceDirectory}")
	private File sourceDirectory;

	@Autowired
	private IgnoreCurrentHourFileFilter ignoreCurrentHourFilter;
	
	@Autowired
	private SetFileNameHeaderTransformer fileNameHeaderTransformer;
	
	@Bean
	public FileReadingMessageSource collectorFileSource() {		
		FileReadingMessageSource source = new FileReadingMessageSource();
		
		source.setDirectory(sourceDirectory);
		source.setFilter(ignoreCurrentHourFilter);
		source.setScanner(collectorDirectoryScanner());
		
		return source;
	}

	@Bean
	public DirectoryScanner collectorDirectoryScanner() {
		DefaultDirectoryScanner scanner = new DefaultDirectoryScanner();
		
		scanner.setFilter(ignoreCurrentHourFilter);
		
		return scanner;
	}
	
	@Bean
	public IntegrationFlow fileScannerFlow() {
		return IntegrationFlows.from(collectorFileSource())
				.transform(fileNameHeaderTransformer)
				.get();
	}
	
	@Bean
	public MessageChannel sourceFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceDataFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceOptionsFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel unprocessableFileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceDataFlowChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel sourceOptionsFlowChannel() {
		return new DirectChannel();
	}
}
