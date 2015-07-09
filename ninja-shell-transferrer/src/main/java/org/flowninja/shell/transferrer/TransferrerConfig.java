/**
 * 
 */
package org.flowninja.shell.transferrer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.flowninja.shell.transferrer.integration.CorrelationKeyBuilderHeaderTransformer;
import org.flowninja.shell.transferrer.integration.DataOrOptionsFlowRouter;
import org.flowninja.shell.transferrer.integration.IgnoreCurrentHourFileFilter;
import org.flowninja.shell.transferrer.integration.SetFileNameHeaderTransformer;
import org.flowninja.shell.transferrer.integration.SourceFileDataFlowParser;
import org.flowninja.shell.transferrer.integration.SourceFileOptionsFlowParser;
import org.flowninja.shell.transferrer.integration.TransferrerConstants;
import org.flowninja.shell.transferrer.integration.UnprocessableFileHandler;
import org.flowninja.types.flows.NetworkFlow;
import org.flowninja.types.flows.NetworkFlowCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;

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
	
	@Autowired
	private CorrelationKeyBuilderHeaderTransformer correlationKeyTransformer;
	
	@Autowired
	private SourceFileDataFlowParser dataFileParser;
	
	@Autowired
	private SourceFileOptionsFlowParser optionsFileParser;

	@Autowired
	private UnprocessableFileHandler unprocessableHandler;
	
	@Autowired
	private DataOrOptionsFlowRouter dataOrOptionsRouter;
	
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
				.transform(correlationKeyTransformer)
				.transform(fileNameHeaderTransformer)
				.<File, String>route(dataOrOptionsRouter::routeDataOrOptionsFlow, 
							m -> m.subFlowMapping("data", dataFileProcessingFlow())
							.subFlowMapping("options", optionsFileProcessingFlow())
							.subFlowMapping("unprocessable", unprocessableFileProcessingFlow()))
				.get();
	}

	@Bean
	public IntegrationFlow dataFileProcessingFlow() {
		return f -> f
				.transform(dataFileParser::parseSingleDataFlowFile)
				.aggregate(a -> a.correlationStrategy(m -> m.getHeaders().get(TransferrerConstants.CORRELATION_HEADER))
						.releaseStrategy(g -> g.size() >= 60)
						.outputProcessor(g -> g.getMessages()
								.stream()
								.flatMap(m -> ((List<?>)m.getPayload()).stream())
								.collect(Collectors.toList()))
						.groupTimeout(5*60*1000L)
						.sendPartialResultOnExpiry(true)
						.expireGroupsUponCompletion(true), null)
				.<List<NetworkFlow>, NetworkFlowCollection>transform(n -> new NetworkFlowCollection(n))
				.handle(n -> System.out.println(n));
	}
	
	@Bean
	public IntegrationFlow optionsFileProcessingFlow() {
		return f -> f.transform(optionsFileParser::parseSingleOptionsFlowFile)
				.aggregate(a -> a.correlationStrategy(m -> m.getHeaders().get(TransferrerConstants.CORRELATION_HEADER))
						.releaseStrategy(g -> g.size() >= 60)
						.outputProcessor(g -> g.getMessages()
								.stream()
								.flatMap(m -> ((List<?>)m.getPayload()).stream())
								.collect(Collectors.toList()))
						.groupTimeout(5*60*1000L)
						.sendPartialResultOnExpiry(true)
						.expireGroupsUponCompletion(true), null)
				.handle(n -> System.out.println(n));
	}

	@Bean
	public IntegrationFlow unprocessableFileProcessingFlow() {
		return f -> f.handle(unprocessableHandler::handleUnprocessableFile);
	}	
}
