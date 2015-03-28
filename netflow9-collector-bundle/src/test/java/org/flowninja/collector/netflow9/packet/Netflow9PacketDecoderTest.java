/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import static org.fest.assertions.api.Assertions.*;

import java.net.InetAddress;
import java.util.Iterator;

import io.netty.channel.embedded.EmbeddedChannel;

import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.Template;
import org.flowninja.collector.common.netflow9.types.TemplateField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class Netflow9PacketDecoderTest {

	private static final InetAddress LOCALHOST;
	
	static {
		try {
			LOCALHOST = InetAddress.getByName("127.0.0.1");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final byte[] dataTemplateOnlyDatagram = new byte[] {
		0x00, 0x09, // Version 9
		0x00, 0x01, // record count: 1
		0x00, 0x1e, 0x2a, 0x48, // sys uptime: 1976904
		0x55, 0x10, 0x6a, (byte)0xb3, // timestamp 1427139251
		0x00, 0x00, 0x00, 0x1f, // packet sequence: 31
		0x00, 0x00, 0x00, 0x00, // source id
		0x00, 0x00, // flow set ID (data template)
		0x00, 0x5c, // template length: 92 octets
		0x01, 0x00, // template ID: 256
		0x00, 0x15, // field count: 21
		0x00, 0x15, 0x00, 0x04, // Field 1: Last switched, length = 4
		0x00, 0x16, 0x00, 0x04, // Field 2: First switched, length = 4
		0x00, 0x01, 0x00, 0x04, // Field 3: Bytes, length = 4
		0x00, 0x02, 0x00, 0x04, // Field 4: Pkts, length = 4
		0x00, 0x0a, 0x00, 0x02, // Field 5: Input SNMP, length = 2
		0x00, 0x0e, 0x00, 0x02, // Field 6: Output SNMP, length = 2
		0x00, 0x08, 0x00, 0x04, // Field 7: IP SRC Addr, length = 4
		0x00, 0x0c, 0x00, 0x04, // Field 8: IP DST Addr, length = 4
		0x00, 0x04, 0x00, 0x01, // Field 9: Protocol, length=1,
		0x00, 0x05, 0x00, 0x01, // Field 10: IP TOS, length=1,
		0x00, 0x07, 0x00, 0x02, // Field 11: L4 SRC Port, length=2
		0x00, 0x0b, 0x00, 0x02, // Field 12: L4 DST Port, length=2
		0x00, 0x30, 0x00, 0x01, // Field 13: Flow sampler ID, length=1
		0x00, 0x33, 0x00, 0x01, // Field 14: Flow class, length=1
		0x00, 0x0f, 0x00, 0x04, // Field 15: IPv4 next hop, length=4
		0x00, 0x0d, 0x00, 0x01, // Field 16: Dst mask, length=1
		0x00, 0x09, 0x00, 0x01, // Field 17: Src mask, length=1
		0x00, 0x06, 0x00, 0x01, // Field 18: TCP Flags, length=1
		0x00, 0x3d, 0x00, 0x01, // Field 19: Direction, length=1
		0x00, 0x11, 0x00, 0x02, // Field 20: Dst AS, length=2
		0x00, 0x10, 0x00, 0x02, // Field 21: Src AS, length=2
	};

	private static final byte[] dataTemplateOneDataFlowDatagram = new byte[] {
		0x00, 0x09, // Version 9
		0x00, 0x02, // record count: 1
		0x00, 0x1e, 0x2a, 0x48, // sys uptime: 1976904
		0x55, 0x10, 0x6a, (byte)0xb3, // timestamp 1427139251
		0x00, 0x00, 0x00, 0x1f, // packet sequence: 31
		0x00, 0x00, 0x00, 0x00, // source id
		0x00, 0x00, // flow set ID (data template)
		0x00, 0x5c, // template length: 92 octets
		0x01, 0x00, // template ID: 256
		0x00, 0x15, // field count: 21
		0x00, 0x15, 0x00, 0x04, // Field 1: Last switched, length = 4
		0x00, 0x16, 0x00, 0x04, // Field 2: First switched, length = 4
		0x00, 0x01, 0x00, 0x04, // Field 3: Bytes, length = 4
		0x00, 0x02, 0x00, 0x04, // Field 4: Pkts, length = 4
		0x00, 0x0a, 0x00, 0x02, // Field 5: Input SNMP, length = 2
		0x00, 0x0e, 0x00, 0x02, // Field 6: Output SNMP, length = 2
		0x00, 0x08, 0x00, 0x04, // Field 7: IP SRC Addr, length = 4
		0x00, 0x0c, 0x00, 0x04, // Field 8: IP DST Addr, length = 4
		0x00, 0x04, 0x00, 0x01, // Field 9: Protocol, length=1,
		0x00, 0x05, 0x00, 0x01, // Field 10: IP TOS, length=1,
		0x00, 0x07, 0x00, 0x02, // Field 11: L4 SRC Port, length=2
		0x00, 0x0b, 0x00, 0x02, // Field 12: L4 DST Port, length=2
		0x00, 0x30, 0x00, 0x01, // Field 13: Flow sampler ID, length=1
		0x00, 0x33, 0x00, 0x01, // Field 14: Flow class, length=1
		0x00, 0x0f, 0x00, 0x04, // Field 15: IPv4 next hop, length=4
		0x00, 0x0d, 0x00, 0x01, // Field 16: Dst mask, length=1
		0x00, 0x09, 0x00, 0x01, // Field 17: Src mask, length=1
		0x00, 0x06, 0x00, 0x01, // Field 18: TCP Flags, length=1
		0x00, 0x3d, 0x00, 0x01, // Field 19: Direction, length=1
		0x00, 0x11, 0x00, 0x02, // Field 20: Dst AS, length=2
		0x00, 0x10, 0x00, 0x02, // Field 21: Src AS, length=2
		
		0x01, 0x00, // Flowset ID: 256
		0x00, 0x34, // Flowset length: 52,
		0x00, 0x1d, (byte)0xed, 0x18, // First switched
		0x00, 0x1d, (byte)0xed, 0x18, // Last switched
		0x00, 0x00, 0x00, 0x43, // Bytes: 67
		0x00, 0x00, 0x00, 0x01, // Pkts: 1
		0x00, 0x04, // Input SNMP
		0x00, 0x05, // Output SNMP
		(byte)0xc0, (byte)0xa8, 0x04, 0x0a, // IP SRC Addr 192.168.4.10
		(byte)0xc0, 0x2b, (byte)0xac, 0x1e, // IP DST Addr 192.43.172.30
		0x11, // Protocol UDP
		0x00, // IP TOS
		(byte)0xb3, (byte)0xd5, // L4 SRC Port 46037
		0x00, 0x35, // L4 DST Port 53
		0x00, // Flow sampler ID 0
		0x00, // Flow class 0
		(byte)0xc0, (byte)0xa8, 0x04, 0x04, // Next hop 192.168.4.4
		0x00, // Dst mask 0
		0x1d, // Src mask 29
		0x10, // TCP flags
		0x01, // Direction Egress
		0x00, 0x00, // DST AS
		0x00, 0x00, // SRC AS
	};

	private static final byte[] dataTemplateTwoDataFlowDatagram = new byte[] {
		0x00, 0x09, // Version 9
		0x00, 0x02, // record count: 1
		0x00, 0x1e, 0x2a, 0x48, // sys uptime: 1976904
		0x55, 0x10, 0x6a, (byte)0xb3, // timestamp 1427139251
		0x00, 0x00, 0x00, 0x1f, // packet sequence: 31
		0x00, 0x00, 0x00, 0x00, // source id
		0x00, 0x00, // flow set ID (data template)
		0x00, 0x5c, // template length: 92 octets
		0x01, 0x00, // template ID: 256
		0x00, 0x15, // field count: 21
		0x00, 0x15, 0x00, 0x04, // Field 1: Last switched, length = 4
		0x00, 0x16, 0x00, 0x04, // Field 2: First switched, length = 4
		0x00, 0x01, 0x00, 0x04, // Field 3: Bytes, length = 4
		0x00, 0x02, 0x00, 0x04, // Field 4: Pkts, length = 4
		0x00, 0x0a, 0x00, 0x02, // Field 5: Input SNMP, length = 2
		0x00, 0x0e, 0x00, 0x02, // Field 6: Output SNMP, length = 2
		0x00, 0x08, 0x00, 0x04, // Field 7: IP SRC Addr, length = 4
		0x00, 0x0c, 0x00, 0x04, // Field 8: IP DST Addr, length = 4
		0x00, 0x04, 0x00, 0x01, // Field 9: Protocol, length=1,
		0x00, 0x05, 0x00, 0x01, // Field 10: IP TOS, length=1,
		0x00, 0x07, 0x00, 0x02, // Field 11: L4 SRC Port, length=2
		0x00, 0x0b, 0x00, 0x02, // Field 12: L4 DST Port, length=2
		0x00, 0x30, 0x00, 0x01, // Field 13: Flow sampler ID, length=1
		0x00, 0x33, 0x00, 0x01, // Field 14: Flow class, length=1
		0x00, 0x0f, 0x00, 0x04, // Field 15: IPv4 next hop, length=4
		0x00, 0x0d, 0x00, 0x01, // Field 16: Dst mask, length=1
		0x00, 0x09, 0x00, 0x01, // Field 17: Src mask, length=1
		0x00, 0x06, 0x00, 0x01, // Field 18: TCP Flags, length=1
		0x00, 0x3d, 0x00, 0x01, // Field 19: Direction, length=1
		0x00, 0x11, 0x00, 0x02, // Field 20: Dst AS, length=2
		0x00, 0x10, 0x00, 0x02, // Field 21: Src AS, length=2
		
		0x01, 0x00, // Flowset ID: 256
		0x00, 0x64, // Flowset length: 100,
		
		0x00, 0x1d, (byte)0xed, 0x18, // First switched
		0x00, 0x1d, (byte)0xed, 0x18, // Last switched
		0x00, 0x00, 0x00, 0x43, // Bytes: 67
		0x00, 0x00, 0x00, 0x01, // Pkts: 1
		0x00, 0x04, // Input SNMP
		0x00, 0x05, // Output SNMP
		(byte)0xc0, (byte)0xa8, 0x04, 0x0a, // IP SRC Addr 192.168.4.10
		(byte)0xc0, 0x2b, (byte)0xac, 0x1e, // IP DST Addr 192.43.172.30
		0x11, // Protocol UDP
		0x00, // IP TOS
		(byte)0xb3, (byte)0xd5, // L4 SRC Port 46037
		0x00, 0x35, // L4 DST Port 53
		0x00, // Flow sampler ID 0
		0x00, // Flow class 0
		(byte)0xc0, (byte)0xa8, 0x04, 0x04, // Next hop 192.168.4.4
		0x00, // Dst mask 0
		0x1d, // Src mask 29
		0x10, // TCP flags
		0x01, // Direction Egress
		0x00, 0x00, // DST AS
		0x00, 0x00, // SRC AS

		0x00, 0x1d, (byte)0xed, 0x1c, // First switched
		0x00, 0x1d, (byte)0xed, 0x1c, // Last switched
		0x00, 0x00, 0x02, (byte)0xfa, // Bytes: 762
		0x00, 0x00, 0x00, 0x01, // Pkts: 1
		0x00, 0x05, // Input SNMP
		0x00, 0x04, // Output SNMP
		(byte)0xc6, 0x29, 0x00, 0x04, // IP SRC Addr 198.41.0.4
		(byte)0xc0, (byte)0xa8, 0x04, 0x0a, // IP DST Addr 192.168.4.10
		0x11, // Protocol UDP
		0x00, // IP TOS
		0x00, 0x35, // L4 SRC Port 53
		0x20, (byte)0xbf, // L4 DST Port 8383
		0x00, // Flow sampler ID 0
		0x00, // Flow class 0
		(byte)0xc0, (byte)0xa8, 0x04, 0x0a, // Next hop 192.168.4.10
		0x1d, // dst mask 29
		0x00, // src mask 0
		0x10, // TCP flags
		0x00, // Direction Ingress
		0x00, 0x00, // DST AS
		0x00, 0x00, // SRC AS

	};

	private PeerRegistry peerRegistry;
	private Netflow9PacketDecoder packetDecoder;
	private EmbeddedChannel channel;
	
	@Before
	public void initTest() {
		peerRegistry = new PeerRegistry();
		packetDecoder = new Netflow9PacketDecoder();
		
		packetDecoder.setPeerRegistry(peerRegistry);
		packetDecoder.setPeerAddressMapper(new StaticAddressPeerAddressMapper());
		
		channel = new EmbeddedChannel(packetDecoder);
	}

	@After
	public void destroyTest() throws Exception {
		channel.close().await();
		
	}
	
	@Test
	public void decodeTemplateOnlyDatagram() {
		channel.writeInbound(channel.alloc().buffer().writeBytes(dataTemplateOnlyDatagram));
		
		assertThat(channel.outboundMessages()).hasSize(0);
		assertThat(peerRegistry.hasRegistryForPeer(LOCALHOST, 0)).isTrue();
		
		FlowRegistry flowRegistry = peerRegistry.registryForPeerAddress(LOCALHOST, 0);

		assertThat(flowRegistry.hasTemplateForFlowsetID(256)).isTrue();
		
		Template template = flowRegistry.templateForFlowsetID(256);
		
		Iterator<TemplateField> fields = template.getFields().iterator();
		
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.LAST_SWITCHED, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.FIRST_SWITCHED, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.IN_BYTES, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.IN_PKTS, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.INPUT_SNMP, 2));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.OUTPUT_SNMP, 2));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.IPV4_SRC_ADDR, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.IPV4_DST_ADDR, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.PROTOCOL, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.TOS, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.L4_SRC_PORT, 2));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.L4_DST_PORT, 2));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.FLOW_SAMPLER_ID, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.FLOW_CLASS, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.IPV4_NEXT_HOP, 4));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.DST_MASK, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.SRC_MASK, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.TCP_FLAGS, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.DIRECTION, 1));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.DST_AS, 2));
		assertThat(fields.next()).has(new TemplateFieldCondition(FieldType.SRC_AS, 2));
	}
	
}
