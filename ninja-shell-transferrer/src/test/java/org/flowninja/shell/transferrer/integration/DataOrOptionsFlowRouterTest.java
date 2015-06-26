/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
public class DataOrOptionsFlowRouterTest {

	private static class CountingHandler implements MessageHandler {

		private int counter = 0;
		
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			counter++;
		}

		/**
		 * @return the counter
		 */
		public int getCounter() {
			return counter;
		}

	}
	
	@Autowired
	@Qualifier("sourceFileChannel")
	private MessageChannel sourceFileChannel;
	
	@Autowired
	@Qualifier("sourceDataFileChannel")
	private SubscribableChannel sourceDataFileChannel;
	
	@Autowired
	@Qualifier("sourceOptionsFileChannel")
	private SubscribableChannel sourceOptionsFileChannel;
	
	@Autowired
	@Qualifier("unprocessableFileChannel")
	private SubscribableChannel unprocessableFileChannel;
	
	private CountingHandler dataHandler;
	private CountingHandler optionsHandler;
	private CountingHandler unprocessableHandler;
	
	@Before
	public void before() {
		dataHandler = new CountingHandler();
		optionsHandler = new CountingHandler();
		unprocessableHandler = new CountingHandler();
		
		sourceDataFileChannel.subscribe(dataHandler);
		sourceOptionsFileChannel.subscribe(optionsHandler);
		unprocessableFileChannel.subscribe(unprocessableHandler);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void routeDataFlow() {
		Date now = new Date();
		String dataFilePrefix = String.format("data-flow-%04d%02d%02d-%02d",
				now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());
		File file = new File(dataFilePrefix);
		
		sourceFileChannel.send(MessageBuilder.withPayload(file).build());
	
		assertThat(dataHandler.getCounter()).isEqualTo(1);
		assertThat(optionsHandler.getCounter()).isEqualTo(0);
		assertThat(unprocessableHandler.getCounter()).isEqualTo(0);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void routeOptionsFlow() {
		Date now = new Date();
		String optionsFilePrefix = String.format("options-flow-%04d%02d%02d-%02d",
				now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());
		File file = new File(optionsFilePrefix);
		
		sourceFileChannel.send(MessageBuilder.withPayload(file).build());
		
		assertThat(dataHandler.getCounter()).isEqualTo(0);
		assertThat(optionsHandler.getCounter()).isEqualTo(1);
		assertThat(unprocessableHandler.getCounter()).isEqualTo(0);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void routeUnknownFlow() {
		Date now = new Date();
		String unknownFilePrefix = String.format("opt-flow-%04d%02d%02d-%02d",
				now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());
		File file = new File(unknownFilePrefix);
		
		sourceFileChannel.send(MessageBuilder.withPayload(file).build());
		
		assertThat(dataHandler.getCounter()).isEqualTo(0);
		assertThat(optionsHandler.getCounter()).isEqualTo(0);
		assertThat(unprocessableHandler.getCounter()).isEqualTo(1);
	}
}
