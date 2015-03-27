/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import static org.fest.assertions.api.Assertions.*;

import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class Netflow9PacketDecoderTest {

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
		0x00, 0x03, 0x00, 0x02, // Field 6: Output SNMP, length = 2
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
		0x00, 0x11, 0x00, 0x00, // Field 20: Dst AS, length=2
		0x00, 0x10, 0x00, 0x00, // Field 21: Src AS, length=2
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
	}
	
}
