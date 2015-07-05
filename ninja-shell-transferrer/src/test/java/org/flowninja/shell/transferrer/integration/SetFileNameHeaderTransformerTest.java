/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=IntegrationTestConfig.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)public class SetFileNameHeaderTransformerTest {
	@Configuration
	public static class TestConfiguration {
		
		@Autowired
		private SetFileNameHeaderTransformer fileNameHeader;
		
		@Autowired
		private SubscribableChannel headerFileInputChannel;

		@Autowired
		private SubscribableChannel headerFileOutputChannel;

		@Bean
		public IntegrationFlow headerFileNameFlow() {
			return IntegrationFlows.from(headerFileInputChannel)
					.transform(fileNameHeader)
					.channel(headerFileOutputChannel)
					.get();
		}
	}
	
	public static class HeaderFlowHandler implements MessageHandler {

		private List<File> files = new LinkedList<File>();
		private List<List<File>> fileLists = new LinkedList<List<File>>();
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			if(message.getHeaders().containsKey(TransferrerConstants.SOURCE_FILE_HEADER))
				files.add(message.getHeaders().get(TransferrerConstants.SOURCE_FILE_HEADER, File.class));
			if(message.getHeaders().containsKey(TransferrerConstants.SOURCE_FILE_LIST_HEADER))
				fileLists.add(message.getHeaders().get(TransferrerConstants.SOURCE_FILE_LIST_HEADER, List.class));
			
		}

		/**
		 * @return the files
		 */
		public List<File> getFiles() {
			return files;
		}

		/**
		 * @return the fileLists
		 */
		public List<List<File>> getFileLists() {
			return fileLists;
		}
		
	}

	@Autowired
	private SubscribableChannel headerFileInputChannel;

	@Autowired
	private SubscribableChannel headerFileOutputChannel;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private HeaderFlowHandler handler;
	
	@Before
	public void before() throws Exception {
		handler = new HeaderFlowHandler();
		
		headerFileOutputChannel.subscribe(handler);
	}
	
	@Test
	public void sendSingleFile() throws Exception {
		File dataFlowFile = resourceLoader.getResource("classpath:data-flow.json").getFile();
		
		headerFileInputChannel.send(MessageBuilder.withPayload(dataFlowFile).build());
				
		assertThat(this.handler.getFiles()).containsExactly(dataFlowFile);
		assertThat(this.handler.getFileLists()).isEmpty();
	}
	
	@Test(expected=MessagingException.class)
	public void sendString() throws Exception {		
		headerFileInputChannel.send(MessageBuilder.withPayload("foo").build());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void sendMultiFile() throws Exception {
		List<File> files = new LinkedList<File>();

		files.add(resourceLoader.getResource("classpath:data-flow.json").getFile());
		
		headerFileInputChannel.send(MessageBuilder.withPayload(files).build());
				
		assertThat(this.handler.getFiles()).isEmpty();
		assertThat(this.handler.getFileLists()).containsExactly(files);
	}
	
}
