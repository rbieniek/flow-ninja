/**
 * 
 */
package org.flowninja.shell.transferrer;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.flowninja.shell.transferrer.integration.CorrelationKeyBuilderHeaderTransformer;
import org.flowninja.shell.transferrer.integration.DataOrOptionsFlowRouter;
import org.flowninja.shell.transferrer.integration.FlowCollectionBuildingTransformer;
import org.flowninja.shell.transferrer.integration.FlowCollectionStorer;
import org.flowninja.shell.transferrer.integration.IgnoreCurrentHourFileFilter;
import org.flowninja.shell.transferrer.integration.IgnoreDuplicateFileFilter;
import org.flowninja.shell.transferrer.integration.ProcessedFileMover;
import org.flowninja.shell.transferrer.integration.SetFileNameHeaderTransformer;
import org.flowninja.shell.transferrer.integration.SourceFileDataFlowParser;
import org.flowninja.shell.transferrer.integration.SourceFileOptionsFlowParser;
import org.flowninja.shell.transferrer.integration.TransferrerConstants;
import org.flowninja.shell.transferrer.integration.UnprocessableFileHandler;
import org.flowninja.types.flows.NetworkFlow;
import org.flowninja.types.flows.OptionsFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.store.MessageGroup;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;

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
	
	@Autowired
	private FlowCollectionBuildingTransformer collectionBuildingTransformer;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private ProcessedFileMover processedMover;
	
	@Autowired
	private FlowCollectionStorer flowCollectionStorer;
	
	@Autowired
	private IgnoreDuplicateFileFilter duplicateFilter;
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public FileReadingMessageSource collectorFileSource() {		
		FileReadingMessageSource source = new FileReadingMessageSource(14400);
		
		source.setDirectory(sourceDirectory);
		source.setScanner(collectorDirectoryScanner());
		
		return source;
	}

	@Bean
	public DirectoryScanner collectorDirectoryScanner() {
		DefaultDirectoryScanner scanner = new DefaultDirectoryScanner();
		
		scanner.setFilter(ignoreCurrentHourFilter);
		
		return scanner;
	}
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {                               // 11
	  	return Pollers.fixedDelay(100, TimeUnit.MILLISECONDS).get();
	}
	
	@Bean
	public IntegrationFlow fileScannerFlow() {
		return IntegrationFlows.from(collectorFileSource(), 
					c -> c.poller(poller()).autoStartup(true))
				.filter(duplicateFilter::canFilePass)
				.transform(correlationKeyTransformer)
				.transform(fileNameHeaderTransformer)
				.<File, String>route(dataOrOptionsRouter::routeDataOrOptionsFlow, 
							m -> m.subFlowMapping("data", dataFileProcessingFlow())
							.subFlowMapping("options", optionsFileProcessingFlow())
							.channelMapping("unprocessable", IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
							)
				.handle(processedMover)
				.get();
	}
	
	@Bean
	public IntegrationFlow dataFileProcessingFlow() {
		return f -> f
				.<File, List<NetworkFlow>>transform(dataFileParser::parseSingleDataFlowFile)
				.aggregate(a -> a
						.outputProcessor(new MessageGroupProcessor() {
							@Override
							public Object processMessageGroup(MessageGroup group) {
								return MessageBuilder.withPayload(group.getMessages()).build();
							}
						})
						.correlationStrategy(m -> m.getHeaders().get(TransferrerConstants.CORRELATION_HEADER))
						.releaseStrategy(g -> g.size() >= 60)
						.groupTimeout(2*60*1000L)
						.sendPartialResultOnExpiry(true)
						.expireGroupsUponCompletion(true), null)
				.transform(collectionBuildingTransformer::collectNetworkFlows)
				.channel(dataTransferChannel())
				;
	}

	@Bean
	public MessageChannel dataTransferChannel() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public IntegrationFlow dataFileTransferFlow() {
		
		return IntegrationFlows.from(dataTransferChannel())
				.transform(flowCollectionStorer::storeNetworkFlowCollection)
				.handle(n -> System.out.println(n))
				.get();
	}
	

	@Bean
	public IntegrationFlow optionsFileProcessingFlow() {
		return f -> f
				.<File, List<OptionsFlow>>transform(optionsFileParser::parseSingleOptionsFlowFile)
				.aggregate(a -> a
						.outputProcessor(new MessageGroupProcessor() {
							@Override
							public Object processMessageGroup(MessageGroup group) {
								return MessageBuilder.withPayload(group.getMessages()).build();
							}
						})
						.correlationStrategy(m -> m.getHeaders().get(TransferrerConstants.CORRELATION_HEADER))
						.releaseStrategy(g -> g.size() >= 60)
						.groupTimeout(5*60*1000L)
						.sendPartialResultOnExpiry(true)
						.expireGroupsUponCompletion(true), null)
				// .handle((p, h) -> { System.out.println(p); return p; })
				;
	}

	@Bean
	public IntegrationFlow unprocessableFileProcessingFlow() {
		return f -> f.handle(unprocessableHandler::handleUnprocessableFile);
	}	
}
