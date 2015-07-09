/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class CorrelationKeyBuilderHeaderTransformerTest {
	@Configuration
	public static class TestConfiguration {
		
		@Autowired
		private CorrelationKeyBuilderHeaderTransformer correlationBuilder;
		
		@Autowired
		private SubscribableChannel correlatorInputChannel;

		@Autowired
		private SubscribableChannel correlatorOutputChannel;

		@Bean
		public IntegrationFlow headerCorrelationFlow() {
			return IntegrationFlows.from(correlatorInputChannel)
					.transform(correlationBuilder)
					.channel(correlatorOutputChannel)
					.get();
		}
	}

	public static class HeaderFlowHandler implements MessageHandler {
		private List<File> files = new LinkedList<>();
		private List<String> headers = new LinkedList<>();

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			if(message.getPayload() instanceof File) {
				files.add((File)message.getPayload());
			}
			
			if(message.getHeaders().containsKey(TransferrerConstants.CORRELATION_HEADER))
				headers.add(message.getHeaders().get(TransferrerConstants.CORRELATION_HEADER, String.class));
		}

		/**
		 * @return the files
		 */
		public List<File> getFiles() {
			return files;
		}

		/**
		 * @return the headers
		 */
		public List<String> getHeaders() {
			return headers;
		}
	}

	@Autowired
	private SubscribableChannel correlatorInputChannel;

	@Autowired
	private SubscribableChannel correlatorOutputChannel;

	private HeaderFlowHandler handler;
	
	@Before
	public void before() throws Exception {
		handler = new HeaderFlowHandler();
		
		correlatorOutputChannel.subscribe(handler);
	}

	@Test
	public void sendDataFile() {
		File data = new File("data-flow-20150907-0901.json");
		
		correlatorInputChannel.send(MessageBuilder.withPayload(data).build());
		
		assertThat(handler.getFiles()).containsExactly(data);
		assertThat(handler.getHeaders()).containsExactly("2015090709");
	}

	@Test
	public void sendOptionsFile() {
		File options = new File("options-flow-20150907-0901.json");
		
		correlatorInputChannel.send(MessageBuilder.withPayload(options).build());
		
		assertThat(handler.getFiles()).containsExactly(options);
		assertThat(handler.getHeaders()).containsExactly("2015090709");
	}

	@Test
	public void badPrefix() {
		File options = new File("opt-flow-20150907-0901.json");
		
		correlatorInputChannel.send(MessageBuilder.withPayload(options).build());
		
		assertThat(handler.getFiles()).containsExactly(options);
		assertThat(handler.getHeaders()).isEmpty();		
	}

	@Test
	public void badSuffix() {
		File data = new File("data-flow-20150907-0901.js");
		
		correlatorInputChannel.send(MessageBuilder.withPayload(data).build());
		
		assertThat(handler.getFiles()).containsExactly(data);
		assertThat(handler.getHeaders()).isEmpty();		
	}
}
