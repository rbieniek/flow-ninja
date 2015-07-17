/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.fest.assertions.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=IntegrationTestConfig.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class IgnoreDuplicateFileFilterTest {
	@Configuration
	public static class TestConfiguration {
		@Autowired
		private DirectChannel acceptOnceInputChannel;

		@Autowired
		private DirectChannel acceptOnceOutputChannel;
		
		@Autowired
		private IgnoreDuplicateFileFilter duplicateFilter;

		@Bean
		public DataSource embeddedDataSource() {
			return (new EmbeddedDatabaseBuilder()).setType(EmbeddedDatabaseType.DERBY).addScript("accept-once-create.sql").build();
		}
		
		@Bean
		public JdbcTemplate jdbcTemplate() {
			return new JdbcTemplate(embeddedDataSource());
		}

		@Bean
		public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(embeddedDataSource());
		}
		
		@Bean
		public IntegrationFlow buildAcceptOnceFlow() {
			return IntegrationFlows.from(acceptOnceInputChannel)
					.filter(duplicateFilter::canFilePass)
					.channel(acceptOnceOutputChannel)
					.get();
		}
	}

	public static class FileFlowHandler implements MessageHandler {
		private Map<File, AtomicInteger> files = new HashMap<File, AtomicInteger>();

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			if(message.getPayload() instanceof File) {
				File file = (File)message.getPayload();
				
				synchronized (files) {
					if(!files.containsKey(file))
						files.put(file, new AtomicInteger(1));
					else
						files.get(file).incrementAndGet();
				}
			}			
		}

		/**
		 * @return the files
		 */
		public Map<File, Integer> getFiles() {
			Map<File, Integer> map = new HashMap<File, Integer>();
			
			files.forEach((k,v) -> map.put(k, v.intValue()));
			
			return map;
		}
	}

	@Autowired
	private DirectChannel acceptOnceInputChannel;

	@Autowired
	private DirectChannel acceptOnceOutputChannel;

	private FileFlowHandler handler;
	
	@Before
	public void before() throws Exception {
		handler = new FileFlowHandler();
		
		acceptOnceOutputChannel.subscribe(handler);
	}

	@Test
	public void singleFilePassing() {
		File file = new File("file-" + UUID.randomUUID().toString());
		
		acceptOnceInputChannel.send(MessageBuilder.withPayload(file).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(file, 1));
	}
	
	@Test
	public void singleFileRepeat() {
		File file = new File("file-" + UUID.randomUUID().toString());
		
		acceptOnceInputChannel.send(MessageBuilder.withPayload(file).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(file, 1));

		acceptOnceInputChannel.send(MessageBuilder.withPayload(file).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(file, 1));
	}

	@Test
	public void twoFilePassing() {
		File fileOne = new File("file-" + UUID.randomUUID().toString());
		File fileTwo = new File("file-" + UUID.randomUUID().toString());
		
		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileOne).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1));

		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileTwo).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1), MapEntry.entry(fileTwo, 1));
	}
	
	@Test
	public void twoFileRepeat() {
		File fileOne = new File("file-" + UUID.randomUUID().toString());
		File fileTwo = new File("file-" + UUID.randomUUID().toString());
		
		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileOne).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1));

		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileTwo).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1), MapEntry.entry(fileTwo, 1));

		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileOne).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1), MapEntry.entry(fileTwo, 1));

		acceptOnceInputChannel.send(MessageBuilder.withPayload(fileTwo).build());
		assertThat(handler.getFiles()).contains(MapEntry.entry(fileOne, 1), MapEntry.entry(fileTwo, 1));
	}
}
