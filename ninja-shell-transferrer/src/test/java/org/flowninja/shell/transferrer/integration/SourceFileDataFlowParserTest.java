/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.types.flows.FlowData;
import org.flowninja.types.flows.FlowDirection;
import org.flowninja.types.flows.FlowHeader;
import org.flowninja.types.flows.NetworkFlow;
import org.flowninja.types.net.ICMPCode;
import org.flowninja.types.net.ICMPType;
import org.flowninja.types.net.IPProtocol;
import org.flowninja.types.net.IPTypeOfService;
import org.flowninja.types.net.TCPFLags;
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
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class SourceFileDataFlowParserTest {
	
	@Configuration
	public static class TestConfiguration {
		@Autowired
		private SourceFileDataFlowParser parser;
		
		@Autowired
		private SetFileNameHeaderTransformer fileNameHeader;
		
		@Autowired
		private SubscribableChannel sourceDataFileChannel;

		@Autowired
		private SubscribableChannel sourceDataFlowChannel;

		@Bean
		public IntegrationFlow dataFlow() {
			return IntegrationFlows.from(sourceDataFileChannel)
					.transform(fileNameHeader)
					.<File, List<NetworkFlow>>transform(parser::parseSingleDataFlowFile)
					.channel(sourceDataFlowChannel)
					.get();
		}
	}

	public static class DataFlowHandler implements MessageHandler {

		private List<NetworkFlow> flows = new LinkedList<NetworkFlow>();
		private List<File> files = new LinkedList<File>();
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			flows.addAll((List<NetworkFlow>)message.getPayload());
			files.add(message.getHeaders().get(TransferrerConstants.SOURCE_FILE_HEADER, File.class));
		}

		/**
		 * @return the flows
		 */
		public List<NetworkFlow> getFlows() {
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
	private SubscribableChannel sourceDataFileChannel;

	@Autowired
	private SubscribableChannel sourceDataFlowChannel;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private DataFlowHandler handler;
	
	@Before
	public void before() throws Exception {
		handler = new DataFlowHandler();
		
		sourceDataFlowChannel.subscribe(handler);
	}
	
	@Test
	public void parseDataFlow() throws Exception {
		File dataFlowFile = resourceLoader.getResource("classpath:data-flow.json").getFile();
		
		sourceDataFileChannel.send(MessageBuilder.withPayload(dataFlowFile).build());
		
		assertThat(this.handler.getFlows()).containsExactly(NetworkFlow.Builder.newBuilder()
				.withPeerIp("192.168.4.9")
				.withFlowUuid("66885c60-abdf-4cbe-bd26-65f3c4cff261")
				.withHeader(FlowHeader.Builder.newBuilder()
						.withRecordCount(23)
						.withSequenceNumber(98749)
						.withSourceId(0)
						.withSysUpTime(417162900)
						.withUnixSeconds(1428092054)
						.build())
				.withData(FlowData.Builder.newBuilder()
						.withLastSwitched(417159468)
						.withFirstSwitched(417158280)
						.withInputBytes(new BigInteger("1278"))
						.withInputPackets(new BigInteger("6"))
						.withInputSnmpIndex(4)
						.withOutputSnmpIndex(5)
						.withIpV4SourceAddress("192.168.1.14")
						.withIpV4DestinationAddress("54.213.3.77")
						.withProtocol(IPProtocol.TCP)
						.withTypeOfService(IPTypeOfService.CS0)
						.withLayer4SourcePort(46305)
						.withLayer4DestinationPort(80)
						.withFlowSamplerID(0)
						.withFlowClass(0)
						.withIpV4NextHopAddress("192.168.4.4")
						.withIpV4DestinationMask(0)
						.withIpV4SourceMask(24)
						.withTcpFlags(TCPFLags.SYN).withTcpFlags(TCPFLags.ACK).withTcpFlags(TCPFLags.PSH).withTcpFlags(TCPFLags.FIN)
						.withFlowDirection(FlowDirection.EGRESS)
						.withMinimumPacketLength(52)
						.withMaximumPacketLength(1010)
						.withIcmpTypeCode(ICMPType.ECHO_REPLY, ICMPCode.UNASSIGNED)
						.withMinimumTimeToLive(63)
						.withMaximumTimeToLive(63)
						.withIpV4Identity(27840)
						.withDestinationAs(0)
						.withSourceAs(0)
						.build())
				.build(),
				NetworkFlow.Builder.newBuilder()
				.withPeerIp("192.168.4.9")
				.withFlowUuid("8911303b-96dc-4852-95cc-531f8b5cc844")
				.withHeader(FlowHeader.Builder.newBuilder()
						.withRecordCount(23)
						.withSequenceNumber(98749)
						.withSourceId(0)
						.withSysUpTime(417162900)
						.withUnixSeconds(1428092054)
						.build())
				.withData(FlowData.Builder.newBuilder()
						.withLastSwitched(417159468)
						.withFirstSwitched(417158656)
						.withInputBytes(new BigInteger("1023"))
						.withInputPackets(new BigInteger("4"))
						.withInputSnmpIndex(5)
						.withOutputSnmpIndex(4)
						.withIpV4SourceAddress("54.213.3.77")
						.withIpV4DestinationAddress("192.168.1.14")
						.withProtocol(IPProtocol.TCP)
						.withTypeOfService(IPTypeOfService.CS0)
						.withLayer4SourcePort(80)
						.withLayer4DestinationPort(46305)
						.withFlowSamplerID(0)
						.withFlowClass(0)
						.withIpV4NextHopAddress("192.168.4.10")
						.withIpV4DestinationMask(24)
						.withIpV4SourceMask(0)
						.withTcpFlags(TCPFLags.SYN).withTcpFlags(TCPFLags.ACK).withTcpFlags(TCPFLags.PSH).withTcpFlags(TCPFLags.FIN)
						.withFlowDirection(FlowDirection.INGRESS)
						.withMinimumPacketLength(52)
						.withMaximumPacketLength(859)
						.withIcmpTypeCode(ICMPType.UNASSIGNED, ICMPCode.UNASSIGNED)
						.withMinimumTimeToLive(39)
						.withMaximumTimeToLive(39)
						.withIpV4Identity(0)
						.withDestinationAs(0)
						.withSourceAs(0)
						.build())
				.build());
		
		assertThat(this.handler.getFiles()).containsExactly(dataFlowFile);
	}
}
