/**
 * 
 */
package org.flowninja.shell.transferrer;

import java.io.File;

import org.flowninja.shell.transferrer.integration.IgnoreCurrentHourFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;

/**
 * @author rainer
 *
 */
@Configuration
@ComponentScan(basePackageClasses=TransferrerConfig.class)
public class TransferrerConfig {
	@Value("#{systemProperties.sourceDirectory}")
	private File sourceDirectory;
	
	@Autowired
	private IgnoreCurrentHourFileFilter ignoreCurrentHourFilter;
	
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
	public SourcePollingChannelAdapter collectorPoller() {
		SourcePollingChannelAdapter adapter = new SourcePollingChannelAdapter();

		adapter.setSource(collectorFileSource());
		
		return adapter;
	}
}
