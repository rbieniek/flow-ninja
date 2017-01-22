/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.ScopeFlowRecord;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.collector.common.types.CounterFactory;
import org.flowninja.collector.common.types.EnumCodeValue;
import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.TestConfig;
import org.flowninja.common.protocol.types.IPProtocol;
import org.flowninja.common.protocol.types.IPTypeOfService;
import org.flowninja.common.protocol.types.TCPFLags;
import org.flowninja.common.types.FlowDirection;
import org.flowninja.common.types.Header;
import org.flowninja.common.types.ScopeType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author rainer
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = TemplateDecoderIntegrationTest.TestConfiguration.class)
@SuppressWarnings("checkstyle:JavaNCSS")
public class TemplateDecoderIntegrationTest {

    private static final InetAddress LOCALHOST = InetAddress.getLoopbackAddress();

    private static final byte[] dataTemplateOnlyDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x01, // record count: 1
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2
    };

    private static final byte[] dataTemplateOneDataFlowDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x02, // record count: 2
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2

            0x01,
            0x00, // Flowset ID: 256
            0x00,
            0x34, // Flowset length: 52,
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // Last switched
            0x00,
            0x00,
            0x00,
            0x43, // Bytes: 67
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x04, // Input SNMP
            0x00,
            0x05, // Output SNMP
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP SRC Addr 192.168.4.10
            (byte) 0xc0,
            0x2b,
            (byte) 0xac,
            0x1e, // IP DST Addr 192.43.172.30
            0x11, // Protocol UDP
            0x00, // IP TOS
            (byte) 0xb3,
            (byte) 0xd5, // L4 SRC Port 46037
            0x00,
            0x35, // L4 DST Port 53
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x04, // Next hop 192.168.4.4
            0x00, // Dst mask 0
            0x1d, // Src mask 29
            0x10, // TCP flags
            0x01, // Direction Egress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS
    };

    private static final byte[] dataTemplateTwoDataFlowDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x02, // record count: 3
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2

            0x01,
            0x00, // Flowset ID: 256
            0x00,
            0x64, // Flowset length: 100,

            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // Last switched
            0x00,
            0x00,
            0x00,
            0x43, // Bytes: 67
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x04, // Input SNMP
            0x00,
            0x05, // Output SNMP
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP SRC Addr 192.168.4.10
            (byte) 0xc0,
            0x2b,
            (byte) 0xac,
            0x1e, // IP DST Addr 192.43.172.30
            0x11, // Protocol UDP
            0x00, // IP TOS
            (byte) 0xb3,
            (byte) 0xd5, // L4 SRC Port 46037
            0x00,
            0x35, // L4 DST Port 53
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x04, // Next hop 192.168.4.4
            0x00, // Dst mask 0
            0x1d, // Src mask 29
            0x10, // TCP flags
            0x01, // Direction Egress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS

            0x00,
            0x1d,
            (byte) 0xed,
            0x1c, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x1c, // Last switched
            0x00,
            0x00,
            0x02,
            (byte) 0xfa, // Bytes: 762
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x05, // Input SNMP
            0x00,
            0x04, // Output SNMP
            (byte) 0xc6,
            0x29,
            0x00,
            0x04, // IP SRC Addr 198.41.0.4
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP DST Addr 192.168.4.10
            0x11, // Protocol UDP
            0x00, // IP TOS
            0x00,
            0x35, // L4 SRC Port 53
            0x20,
            (byte) 0xbf, // L4 DST Port 8383
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // Next hop 192.168.4.10
            0x1d, // dst mask 29
            0x00, // src mask 0
            0x10, // TCP flags
            0x00, // Direction Ingress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS

    };

    private static final byte[] optionsTemplateOnlyDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x01, // record count: 1
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x01, // flow set ID (options template)
            0x00,
            0x18, // length: 24 octets
            0x01,
            0x04, // Template ID: 260
            0x00,
            0x04, // Scope length: 4 octets,
            0x00,
            0x08, // Options length: 8 octets
            0x00,
            0x01, // Scope type: System
            0x00,
            0x00, // Length: 0 octets
            0x00,
            0x2a, // Type: Total flows exported (code 42)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x29, // Type: Total packets exported (code 41)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x00, // Padding

    };

    private static final byte[] optionsTemplateAndOptionsDataDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x01, // record count: 1
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id
            0x00,
            0x01, // flow set ID (options template)
            0x00,
            0x18, // length: 24 octets
            0x01,
            0x04, // Template ID: 260
            0x00,
            0x04, // Scope length: 4 octets,
            0x00,
            0x08, // Options length: 8 octets
            0x00,
            0x01, // Scope type: System
            0x00,
            0x00, // Length: 0 octets
            0x00,
            0x2a, // Type: Total flows exported (code 42)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x29, // Type: Total packets exported (code 41)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x00, // Padding

            0x01,
            0x04, // Flow set ID: 260
            0x00,
            0x0c, // Length: 12 octets
            0x00,
            0x04,
            (byte) 0xbc,
            (byte) 0xd7, // Flows export 310487
            0x00,
            0x00,
            0x35,
            0x52, // packets exported 13560
    };

    private static final byte[] optionsAndDataTemplateDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x01, // record count: 2
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id

            0x00,
            0x01, // flow set ID (options template)
            0x00,
            0x18, // length: 24 octets
            0x01,
            0x04, // Template ID: 260
            0x00,
            0x04, // Scope length: 4 octets,
            0x00,
            0x08, // Options length: 8 octets
            0x00,
            0x01, // Scope type: System
            0x00,
            0x00, // Length: 0 octets
            0x00,
            0x2a, // Type: Total flows exported (code 42)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x29, // Type: Total packets exported (code 41)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x00, // Padding

            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2
    };

    private static final byte[] dataAndOptionsTemplateOneDataFlowOneOptionFlowDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x02, // record count: 4
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id

            0x00,
            0x00, // flow set ID (data template)
            0x00,
            0x5c, // template length: 92 octets
            0x01,
            0x00, // template ID: 256
            0x00,
            0x15, // field count: 21
            0x00,
            0x15,
            0x00,
            0x04, // Field 1: Last switched, length = 4
            0x00,
            0x16,
            0x00,
            0x04, // Field 2: First switched, length = 4
            0x00,
            0x01,
            0x00,
            0x04, // Field 3: Bytes, length = 4
            0x00,
            0x02,
            0x00,
            0x04, // Field 4: Pkts, length = 4
            0x00,
            0x0a,
            0x00,
            0x02, // Field 5: Input SNMP, length = 2
            0x00,
            0x0e,
            0x00,
            0x02, // Field 6: Output SNMP, length = 2
            0x00,
            0x08,
            0x00,
            0x04, // Field 7: IP SRC Addr, length = 4
            0x00,
            0x0c,
            0x00,
            0x04, // Field 8: IP DST Addr, length = 4
            0x00,
            0x04,
            0x00,
            0x01, // Field 9: Protocol, length=1,
            0x00,
            0x05,
            0x00,
            0x01, // Field 10: IP TOS, length=1,
            0x00,
            0x07,
            0x00,
            0x02, // Field 11: L4 SRC Port, length=2
            0x00,
            0x0b,
            0x00,
            0x02, // Field 12: L4 DST Port, length=2
            0x00,
            0x30,
            0x00,
            0x01, // Field 13: Flow sampler ID, length=1
            0x00,
            0x33,
            0x00,
            0x01, // Field 14: Flow class, length=1
            0x00,
            0x0f,
            0x00,
            0x04, // Field 15: IPv4 next hop, length=4
            0x00,
            0x0d,
            0x00,
            0x01, // Field 16: Dst mask, length=1
            0x00,
            0x09,
            0x00,
            0x01, // Field 17: Src mask, length=1
            0x00,
            0x06,
            0x00,
            0x01, // Field 18: TCP Flags, length=1
            0x00,
            0x3d,
            0x00,
            0x01, // Field 19: Direction, length=1
            0x00,
            0x11,
            0x00,
            0x02, // Field 20: Dst AS, length=2
            0x00,
            0x10,
            0x00,
            0x02, // Field 21: Src AS, length=2

            0x00,
            0x01, // flow set ID (options template)
            0x00,
            0x18, // length: 24 octets
            0x01,
            0x04, // Template ID: 260
            0x00,
            0x04, // Scope length: 4 octets,
            0x00,
            0x08, // Options length: 8 octets
            0x00,
            0x01, // Scope type: System
            0x00,
            0x00, // Length: 0 octets
            0x00,
            0x2a, // Type: Total flows exported (code 42)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x29, // Type: Total packets exported (code 41)
            0x00,
            0x04, // Length: 4 octets
            0x00,
            0x00, // Padding

            0x01,
            0x00, // Flowset ID: 256
            0x00,
            0x34, // Flowset length: 52,
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // Last switched
            0x00,
            0x00,
            0x00,
            0x43, // Bytes: 67
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x04, // Input SNMP
            0x00,
            0x05, // Output SNMP
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP SRC Addr 192.168.4.10
            (byte) 0xc0,
            0x2b,
            (byte) 0xac,
            0x1e, // IP DST Addr 192.43.172.30
            0x11, // Protocol UDP
            0x00, // IP TOS
            (byte) 0xb3,
            (byte) 0xd5, // L4 SRC Port 46037
            0x00,
            0x35, // L4 DST Port 53
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x04, // Next hop 192.168.4.4
            0x00, // Dst mask 0
            0x1d, // Src mask 29
            0x10, // TCP flags
            0x01, // Direction Egress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS

            0x01,
            0x04, // Flow set ID: 260
            0x00,
            0x0c, // Length: 12 octets
            0x00,
            0x04,
            (byte) 0xbc,
            (byte) 0xd7, // Flows export 310487
            0x00,
            0x00,
            0x35,
            0x52, // packets exported 13560
    };

    private static final byte[] dataFlowOnlyDatagram = new byte[] {
            0x00,
            0x09, // Version
                  // 9
            0x00,
            0x02, // record count: 1
            0x00,
            0x1e,
            0x2a,
            0x48, // sys uptime: 1976904
            0x55,
            0x10,
            0x6a,
            (byte) 0xb3, // timestamp 1427139251
            0x00,
            0x00,
            0x00,
            0x1f, // packet sequence: 31
            0x00,
            0x00,
            0x00,
            0x00, // source id

            0x01,
            0x00, // Flowset ID: 256
            0x00,
            0x34, // Flowset length: 52,
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // First switched
            0x00,
            0x1d,
            (byte) 0xed,
            0x18, // Last switched
            0x00,
            0x00,
            0x00,
            0x43, // Bytes: 67
            0x00,
            0x00,
            0x00,
            0x01, // Pkts: 1
            0x00,
            0x04, // Input SNMP
            0x00,
            0x05, // Output SNMP
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x0a, // IP SRC Addr 192.168.4.10
            (byte) 0xc0,
            0x2b,
            (byte) 0xac,
            0x1e, // IP DST Addr 192.43.172.30
            0x11, // Protocol UDP
            0x00, // IP TOS
            (byte) 0xb3,
            (byte) 0xd5, // L4 SRC Port 46037
            0x00,
            0x35, // L4 DST Port 53
            0x00, // Flow sampler ID 0
            0x00, // Flow class 0
            (byte) 0xc0,
            (byte) 0xa8,
            0x04,
            0x04, // Next hop 192.168.4.4
            0x00, // Dst mask 0
            0x1d, // Src mask 29
            0x10, // TCP flags
            0x01, // Direction Egress
            0x00,
            0x00, // DST AS
            0x00,
            0x00, // SRC AS
    };

    @Autowired
    private EmbeddedChannel channel;

    @Autowired
    private FlowsetDecoderChannel datagramSinkChannel;

    @After
    public void clearSink() {
        datagramSinkChannel.clear();
    }

    public DatagramPacket buildPacket(final Channel ch, final byte[] data) {
        return new DatagramPacket(
                channel.alloc().buffer().writeBytes(data),
                new InetSocketAddress(LOCALHOST, 2055),
                new InetSocketAddress(LOCALHOST, 49152));
    }

    @Test
    public void decodeDataTemplateOnlyDatagram() {
        channel.writeInbound(buildPacket(channel, dataTemplateOnlyDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();
    }

    @Test
    public void decodeOptionsTemplateOnlyDatagram() {
        channel.writeInbound(buildPacket(channel, optionsTemplateOnlyDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();

        assertThat(datagramSinkChannel.getDataTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();
    }

    @Test
    public void decodeDataTemplateOneDataFlowDatagram() throws Exception {
        channel.writeInbound(buildPacket(channel, dataTemplateOneDataFlowDatagram));

        assertThat(datagramSinkChannel.getDataFlows()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        final DataFlow flow = datagramSinkChannel.getDataFlows().removeFirst();

        assertThat(flow.getUuid()).isNotNull();

        final Header header = flow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(2);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        final Iterator<FlowValueRecord> it = flow.getRecords().iterator();

        assertDataFlowRecord(it, FieldType.LAST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.FIRST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.IN_BYTES, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x43 }));
        assertDataFlowRecord(it, FieldType.IN_PKTS, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
        assertDataFlowRecord(it, FieldType.INPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x04 }));
        assertDataFlowRecord(it, FieldType.OUTPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x05 }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_SRC_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_DST_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, 0x2b, (byte) 0xac, 0x1e }));
        assertDataFlowRecord(it, FieldType.PROTOCOL, EnumCodeValue.builder().value(IPProtocol.UDP).code(0x11).build());
        assertDataFlowRecord(it, FieldType.SRC_TOS, EnumCodeValue.builder().value(IPTypeOfService.CS0).code(0x00).build());
        assertDataFlowRecord(it, FieldType.L4_SRC_PORT, Integer.valueOf(0xb3d5));
        assertDataFlowRecord(it, FieldType.L4_DST_PORT, Integer.valueOf(53));
        assertDataFlowRecord(it, FieldType.FLOW_SAMPLER_ID, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.FLOW_CLASS, Integer.valueOf(0));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_NEXT_HOP,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x04 }));
        assertDataFlowRecord(it, FieldType.DST_MASK, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_MASK, Integer.valueOf(29));
        assertDataFlowRecord(it, FieldType.TCP_FLAGS, TCPFLags.class, TCPFLags.ACK);
        assertDataFlowRecord(it, FieldType.DIRECTION, EnumCodeValue.builder().value(FlowDirection.EGRESS).code(0x01).build());
        assertDataFlowRecord(it, FieldType.DST_AS, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_AS, Integer.valueOf(0));

        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void decodeDataTemplateTwoDataFlowDatagram() throws Exception {
        channel.writeInbound(buildPacket(channel, dataTemplateTwoDataFlowDatagram));

        assertThat(datagramSinkChannel.getDataFlows()).hasSize(2);
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        DataFlow flow = datagramSinkChannel.getDataFlows().removeFirst();
        Header header = flow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(2);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        Iterator<FlowValueRecord> it = flow.getRecords().iterator();

        assertDataFlowRecord(it, FieldType.LAST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.FIRST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.IN_BYTES, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x43 }));
        assertDataFlowRecord(it, FieldType.IN_PKTS, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
        assertDataFlowRecord(it, FieldType.INPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x04 }));
        assertDataFlowRecord(it, FieldType.OUTPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x05 }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_SRC_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_DST_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, 0x2b, (byte) 0xac, 0x1e }));
        assertDataFlowRecord(it, FieldType.PROTOCOL, EnumCodeValue.builder().value(IPProtocol.UDP).code(0x11).build());
        assertDataFlowRecord(it, FieldType.SRC_TOS, EnumCodeValue.builder().value(IPTypeOfService.CS0).code(0x00).build());
        assertDataFlowRecord(it, FieldType.L4_SRC_PORT, Integer.valueOf(0xb3d5));
        assertDataFlowRecord(it, FieldType.L4_DST_PORT, Integer.valueOf(53));
        assertDataFlowRecord(it, FieldType.FLOW_SAMPLER_ID, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.FLOW_CLASS, Integer.valueOf(0));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_NEXT_HOP,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x04 }));
        assertDataFlowRecord(it, FieldType.DST_MASK, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_MASK, Integer.valueOf(29));
        assertDataFlowRecord(it, FieldType.TCP_FLAGS, TCPFLags.class, TCPFLags.ACK);
        assertDataFlowRecord(it, FieldType.DIRECTION, EnumCodeValue.builder().value(FlowDirection.EGRESS).code(0x01).build());
        assertDataFlowRecord(it, FieldType.DST_AS, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_AS, Integer.valueOf(0));

        assertThat(it.hasNext()).isFalse();

        flow = datagramSinkChannel.getDataFlows().removeFirst();
        header = flow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(2);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        it = flow.getRecords().iterator();

        assertDataFlowRecord(it, FieldType.LAST_SWITCHED, Long.valueOf(0x001ded1c));
        assertDataFlowRecord(it, FieldType.FIRST_SWITCHED, Long.valueOf(0x001ded1c));
        assertDataFlowRecord(it, FieldType.IN_BYTES, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x02, (byte) 0xfa }));
        assertDataFlowRecord(it, FieldType.IN_PKTS, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
        assertDataFlowRecord(it, FieldType.INPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x05 }));
        assertDataFlowRecord(it, FieldType.OUTPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x04 }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_SRC_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc6, 0x29, 0x00, 0x04 }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_DST_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(it, FieldType.PROTOCOL, EnumCodeValue.builder().value(IPProtocol.UDP).code(0x11).build());
        assertDataFlowRecord(it, FieldType.SRC_TOS, EnumCodeValue.builder().value(IPTypeOfService.CS0).code(0x00).build());
        assertDataFlowRecord(it, FieldType.L4_SRC_PORT, Integer.valueOf(53));
        assertDataFlowRecord(it, FieldType.L4_DST_PORT, Integer.valueOf(0x20bf));
        assertDataFlowRecord(it, FieldType.FLOW_SAMPLER_ID, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.FLOW_CLASS, Integer.valueOf(0));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_NEXT_HOP,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(it, FieldType.DST_MASK, Integer.valueOf(29));
        assertDataFlowRecord(it, FieldType.SRC_MASK, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.TCP_FLAGS, TCPFLags.class, TCPFLags.ACK);
        assertDataFlowRecord(it, FieldType.DIRECTION, EnumCodeValue.builder().value(FlowDirection.INGRESS).code(0x00).build());
        assertDataFlowRecord(it, FieldType.DST_AS, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_AS, Integer.valueOf(0));

        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void decodeOptionsTemplateAndOptionsDataDatagram() {
        channel.writeInbound(buildPacket(channel, optionsTemplateAndOptionsDataDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsFlows()).hasSize(1);
        assertThat(datagramSinkChannel.getDataTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        final OptionsFlow flow = datagramSinkChannel.getOptionsFlows().remove();

        assertThat(flow.getUuid()).isNotNull();

        final Header header = flow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(1);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        final Iterator<ScopeFlowRecord> scopeIterator = flow.getScopes().iterator();

        assertScopeFlowRecord(scopeIterator, ScopeType.SYSTEM, null);
        assertThat(scopeIterator.hasNext()).isFalse();

        final Iterator<FlowValueRecord> optionsIterator = flow.getRecords().iterator();

        assertOptionsFlowRecord(
                optionsIterator,
                FieldType.TOTAL_FLOWS_EXP,
                CounterFactory.decode(new byte[] { 0x00, 0x04, (byte) 0xbc, (byte) 0xd7 }));
        assertOptionsFlowRecord(
                optionsIterator,
                FieldType.TOTAL_PKTS_EXP,
                CounterFactory.decode(new byte[] { 0x00, 0x00, 0x35, 0x52 }));
        assertThat(optionsIterator.hasNext()).isFalse();

    }

    @Test
    public void decodeOptionsAndDataTemplateDatagram() {
        channel.writeInbound(buildPacket(channel, optionsAndDataTemplateDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();
    }

    @Test
    public void decodeDataAndOptionsTemplateOneDataFlowOneOptionFlowDatagram() throws Exception {
        channel.writeInbound(buildPacket(channel, dataAndOptionsTemplateOneDataFlowOneOptionFlowDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsFlows()).hasSize(1);
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        final DataFlow dataFlow = datagramSinkChannel.getDataFlows().remove();
        final Header header = dataFlow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(2);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        final Iterator<FlowValueRecord> dataIt = dataFlow.getRecords().iterator();

        assertDataFlowRecord(dataIt, FieldType.LAST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(dataIt, FieldType.FIRST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(dataIt, FieldType.IN_BYTES, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x43 }));
        assertDataFlowRecord(dataIt, FieldType.IN_PKTS, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
        assertDataFlowRecord(dataIt, FieldType.INPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x04 }));
        assertDataFlowRecord(dataIt, FieldType.OUTPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x05 }));
        assertDataFlowRecord(
                dataIt,
                FieldType.IPV4_SRC_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(
                dataIt,
                FieldType.IPV4_DST_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, 0x2b, (byte) 0xac, 0x1e }));
        assertDataFlowRecord(dataIt, FieldType.PROTOCOL, EnumCodeValue.builder().value(IPProtocol.UDP).code(0x11).build());
        assertDataFlowRecord(dataIt, FieldType.SRC_TOS, EnumCodeValue.builder().value(IPTypeOfService.CS0).code(0x00).build());
        assertDataFlowRecord(dataIt, FieldType.L4_SRC_PORT, Integer.valueOf(0xb3d5));
        assertDataFlowRecord(dataIt, FieldType.L4_DST_PORT, Integer.valueOf(53));
        assertDataFlowRecord(dataIt, FieldType.FLOW_SAMPLER_ID, Integer.valueOf(0));
        assertDataFlowRecord(dataIt, FieldType.FLOW_CLASS, Integer.valueOf(0));
        assertDataFlowRecord(
                dataIt,
                FieldType.IPV4_NEXT_HOP,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x04 }));
        assertDataFlowRecord(dataIt, FieldType.DST_MASK, Integer.valueOf(0));
        assertDataFlowRecord(dataIt, FieldType.SRC_MASK, Integer.valueOf(29));
        assertDataFlowRecord(dataIt, FieldType.TCP_FLAGS, TCPFLags.class, TCPFLags.ACK);
        assertDataFlowRecord(
                dataIt,
                FieldType.DIRECTION,
                EnumCodeValue.builder().value(FlowDirection.EGRESS).code(0x01).build());
        assertDataFlowRecord(dataIt, FieldType.DST_AS, Integer.valueOf(0));
        assertDataFlowRecord(dataIt, FieldType.SRC_AS, Integer.valueOf(0));

        assertThat(dataIt.hasNext()).isFalse();

        final OptionsFlow scopeFlow = datagramSinkChannel.getOptionsFlows().remove();
        final Header scopeHeader = scopeFlow.getHeader();

        assertThat(scopeHeader).isNotNull();
        assertThat(scopeHeader.getRecordCount()).isEqualTo(2);
        assertThat(scopeHeader.getSysUpTime()).isEqualTo(1976904);
        assertThat(scopeHeader.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(scopeHeader.getSequenceNumber()).isEqualTo(31);
        assertThat(scopeHeader.getSourceId()).isEqualTo(0);

        final Iterator<ScopeFlowRecord> scopeIterator = scopeFlow.getScopes().iterator();

        assertScopeFlowRecord(scopeIterator, ScopeType.SYSTEM, null);
        assertThat(scopeIterator.hasNext()).isFalse();

        final Iterator<FlowValueRecord> optionsIterator = scopeFlow.getRecords().iterator();

        assertOptionsFlowRecord(
                optionsIterator,
                FieldType.TOTAL_FLOWS_EXP,
                CounterFactory.decode(new byte[] { 0x00, 0x04, (byte) 0xbc, (byte) 0xd7 }));
        assertOptionsFlowRecord(
                optionsIterator,
                FieldType.TOTAL_PKTS_EXP,
                CounterFactory.decode(new byte[] { 0x00, 0x00, 0x35, 0x52 }));
        assertThat(optionsIterator.hasNext()).isFalse();
    }

    @Test
    public void decodeDataFlowDatagramAftereDataTemplate() throws Exception {
        channel.writeInbound(buildPacket(channel, dataTemplateOnlyDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).isEmpty();
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        channel.writeInbound(buildPacket(channel, dataFlowOnlyDatagram));

        assertThat(channel.inboundMessages()).isEmpty();
        assertThat(datagramSinkChannel.getDataFlows()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsFlows()).isEmpty();
        assertThat(datagramSinkChannel.getDataTemplates()).hasSize(1);
        assertThat(datagramSinkChannel.getOptionsTemplates()).isEmpty();
        assertThat(datagramSinkChannel.getUnknownFlowsetIds()).isEmpty();

        final DataFlow flow = datagramSinkChannel.getDataFlows().remove();
        final Header header = flow.getHeader();

        assertThat(header).isNotNull();
        assertThat(header.getRecordCount()).isEqualTo(2);
        assertThat(header.getSysUpTime()).isEqualTo(1976904);
        assertThat(header.getUnixSeconds()).isEqualTo(1427139251);
        assertThat(header.getSequenceNumber()).isEqualTo(31);
        assertThat(header.getSourceId()).isEqualTo(0);

        final Iterator<FlowValueRecord> it = flow.getRecords().iterator();

        assertDataFlowRecord(it, FieldType.LAST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.FIRST_SWITCHED, Long.valueOf(0x001ded18));
        assertDataFlowRecord(it, FieldType.IN_BYTES, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x43 }));
        assertDataFlowRecord(it, FieldType.IN_PKTS, CounterFactory.decode(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
        assertDataFlowRecord(it, FieldType.INPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x04 }));
        assertDataFlowRecord(it, FieldType.OUTPUT_SNMP, CounterFactory.decode(new byte[] { 0x00, 0x05 }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_SRC_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x0a }));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_DST_ADDR,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, 0x2b, (byte) 0xac, 0x1e }));
        assertDataFlowRecord(it, FieldType.PROTOCOL, EnumCodeValue.builder().value(IPProtocol.UDP).code(0x11).build());
        assertDataFlowRecord(it, FieldType.SRC_TOS, EnumCodeValue.builder().value(IPTypeOfService.CS0).code(0x00).build());
        assertDataFlowRecord(it, FieldType.L4_SRC_PORT, Integer.valueOf(0xb3d5));
        assertDataFlowRecord(it, FieldType.L4_DST_PORT, Integer.valueOf(53));
        assertDataFlowRecord(it, FieldType.FLOW_SAMPLER_ID, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.FLOW_CLASS, Integer.valueOf(0));
        assertDataFlowRecord(
                it,
                FieldType.IPV4_NEXT_HOP,
                Inet4Address.getByAddress(new byte[] { (byte) 0xc0, (byte) 0xa8, 0x04, 0x04 }));
        assertDataFlowRecord(it, FieldType.DST_MASK, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_MASK, Integer.valueOf(29));
        assertDataFlowRecord(it, FieldType.TCP_FLAGS, TCPFLags.class, TCPFLags.ACK);
        assertDataFlowRecord(it, FieldType.DIRECTION, EnumCodeValue.builder().value(FlowDirection.EGRESS).code(0x01).build());
        assertDataFlowRecord(it, FieldType.DST_AS, Integer.valueOf(0));
        assertDataFlowRecord(it, FieldType.SRC_AS, Integer.valueOf(0));

        assertThat(it.hasNext()).isFalse();
    }

    private void assertDataFlowRecord(final Iterator<FlowValueRecord> it, final FieldType type, final Object value) {
        final FlowValueRecord dfr;

        assertThat(it.hasNext()).isTrue();
        dfr = it.next();
        assertThat(dfr.getType()).isEqualTo(type);
        assertThat(dfr.getValue()).isEqualTo(value);

    }

    private void assertOptionsFlowRecord(final Iterator<FlowValueRecord> it, final FieldType type, final Object value) {
        final FlowValueRecord dfr;

        assertThat(it.hasNext()).isTrue();
        dfr = it.next();
        assertThat(dfr.getType()).isEqualTo(type);
        assertThat(dfr.getValue()).isEqualTo(value);

    }

    private void assertScopeFlowRecord(final Iterator<ScopeFlowRecord> it, final ScopeType type, final Counter value) {
        final ScopeFlowRecord dfr;

        assertThat(it.hasNext()).isTrue();
        dfr = it.next();
        assertThat(dfr.getType()).isEqualTo(type);

        if (value != null) {
            assertThat(dfr.getValue()).isEqualTo(value);
        } else {
            assertThat(dfr.getValue()).isNull();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void assertDataFlowRecord(
            final Iterator<FlowValueRecord> it,
            final FieldType type,
            final Class<T> clazz,
            final T... values) {
        final FlowValueRecord dfr;

        assertThat(it.hasNext()).isTrue();
        dfr = it.next();
        assertThat(dfr.getType()).isEqualTo(type);
        assertThat((Set<T>) dfr.getValue()).containsOnly(values);

    }

    @Getter
    @Sharable
    @RequiredArgsConstructor
    public static class FlowsetDecoderChannel extends ChannelInboundHandlerAdapter {

        private final TemplateDecoder templateDecoder;

        private Map<Integer, DataTemplate> dataTemplates = new HashMap<>();
        private Map<Integer, OptionsTemplate> optionsTemplates = new HashMap<>();
        private Set<Integer> unknownFlowsetIds = new HashSet<>();
        private Deque<DataFlow> dataFlows = new LinkedList<>();
        private Deque<OptionsFlow> optionsFlows = new LinkedList<>();

        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            if (msg instanceof Netflow9DecodedDatagram) {
                final Netflow9DecodedDatagram decoded = (Netflow9DecodedDatagram) msg;

                decoded.getTemplates().forEach(t -> dataTemplates.put(t.getFlowsetId(), t));
                decoded.getOptionsTemplates().forEach(t -> optionsTemplates.put(t.getFlowsetId(), t));

                decoded.getFlows().forEach(fb -> {
                    final int flowsetId = fb.getFlowSetId();

                    if (dataTemplates.containsKey(flowsetId)) {
                        dataFlows.addAll(
                                templateDecoder.decodeDataTemplate(decoded.getPeerAddress(), fb, dataTemplates.get(flowsetId)));
                    } else if (optionsTemplates.containsKey(flowsetId)) {
                        optionsFlows.addAll(
                                templateDecoder
                                        .decodeOptionsTemplate(decoded.getPeerAddress(), fb, optionsTemplates.get(flowsetId)));
                    } else {
                        unknownFlowsetIds.add(flowsetId);
                    }
                });
            }
        }

        public void clear() {
            dataTemplates.clear();
            optionsTemplates.clear();
            unknownFlowsetIds.clear();
            dataFlows.clear();
            optionsFlows.clear();
        }

    }

    @TestConfig
    public static class TestConfiguration {

        @Bean
        @Autowired
        public EmbeddedChannel embeddedChannel(
                final Netflow9DatagramDecoder netflow9DatagramDecoder,
                final FlowsetDecoderChannel datagramSinkChannel) {
            return new EmbeddedChannel(netflow9DatagramDecoder, datagramSinkChannel);
        }

        @Bean
        @Autowired
        public FlowsetDecoderChannel datagramSinkChannel(final TemplateDecoder templateDecoder) {
            return new FlowsetDecoderChannel(templateDecoder);
        }

        @Bean
        public TemplateDecoder templateDecoder() {
            return new TemplateDecoder();
        }

        @Bean
        public PeerAddressMapper peerAddressMapper() {
            return new InetSocketAddressPeerAddressMapper();
        }

        @Bean
        @Autowired
        public Netflow9DatagramDecoder netflow9DatagramDecoder(final PeerAddressMapper peerAddressMapper) {
            return new Netflow9DatagramDecoder(peerAddressMapper);
        }
    }
}
