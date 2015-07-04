/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.types.flows.FlowHeader;
import org.flowninja.types.flows.FlowScope;
import org.flowninja.types.flows.OptionsData;
import org.flowninja.types.flows.OptionsFlow;
import org.flowninja.types.flows.ScopeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
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
public class SourceFileOptionsFlowParserTest {

	public static class OptionsFlowHandler implements MessageHandler {

		private List<OptionsFlow> flows = new LinkedList<OptionsFlow>();
		private List<File> files = new LinkedList<File>();

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			flows.addAll((List<OptionsFlow>)message.getPayload());
			files.add(message.getHeaders().get(TransferrerConstants.SOURCE_FILE_HEADER, File.class));
		}

		/**
		 * @return the flows
		 */
		public List<OptionsFlow> getFlows() {
			return flows;
		}
		

		/**
		 * @return the files
		 */
		public List<File> getFiles() {
			return files;
		}
	}
	
	@Autowired
	@Qualifier("sourceOptionsFileChannel")
	private SubscribableChannel sourceOptionsFileChannel;

	@Autowired
	@Qualifier("sourceOptionsFlowChannel")
	private SubscribableChannel sourceOptionsFlowChannel;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private OptionsFlowHandler handler;
	
	@Before
	public void before() throws Exception {
		handler = new OptionsFlowHandler();
		
		sourceOptionsFlowChannel.subscribe(handler);
	}
	
	@Test
	public void parseDataFlow() throws Exception {
		File dataFlowFile = resourceLoader.getResource("classpath:options-flow.json").getFile();
		
		sourceOptionsFileChannel.send(MessageBuilder.withPayload(dataFlowFile).build());
		
		assertThat(this.handler.getFlows()).containsExactly(OptionsFlow.Builder.newBuilder()
				.withPeerIp("192.168.4.9")
				.withFlowUuid("e4e75b1a-dc68-4b8d-a234-234fb8a0cc00")
				.withHeader(FlowHeader.Builder.newBuilder()
						.withRecordCount(12)
						.withSequenceNumber(515370)
						.withSourceId(0)
						.withSysUpTime(1978648900)
						.withUnixSeconds(1429653540)
						.build())
				.withFlowScope(FlowScope.Builder.newBuilder()
						.withType(ScopeType.SYSTEM)
						.build())
				.withData(OptionsData.Builder.newBuilder()
						.withTotalFlowsExported(new BigInteger("11788997"))
						.withTotalPacketsExported(new BigInteger("515370"))
						.build())
				.build());
		
		assertThat(this.handler.getFiles()).containsExactly(dataFlowFile);
	}
}
