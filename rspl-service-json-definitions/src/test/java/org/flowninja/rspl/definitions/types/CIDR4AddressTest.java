/**
 * 
 */
package org.flowninja.rspl.definitions.types;

import static org.fest.assertions.api.Assertions.*;

import java.net.Inet4Address;

import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class CIDR4AddressTest {
	private static final byte[] IP_ADDR_192_168_64_33 = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 };
	private static final byte[] IP_ADDR_172_16_192_99 = new byte[] { (byte)0xac, (byte)0x10, (byte)0xc0, (byte)0x63 };
	
	private Inet4Address ipAddress_192_168_64_33;
	private Inet4Address ipAddress_172_16_192_99;
	
	@Before
	public void init() throws Exception {
		this.ipAddress_172_16_192_99 = (Inet4Address)Inet4Address.getByAddress(IP_ADDR_172_16_192_99);
		this.ipAddress_192_168_64_33 = (Inet4Address)Inet4Address.getByAddress(IP_ADDR_192_168_64_33);
	}
	
	@Test
	public void netmask28() {
		CIDR4Address addr = new CIDR4Address(IP_ADDR_192_168_64_33, 28);

		assertThat(addr).isNotNull();
		assertThat(addr.getAddress()).isEqualTo(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x20 });
		assertThat(addr.getNetmask()).isEqualTo(28);
		assertThat(addr.toString()).isEqualTo("192.168.64.32/28");
		assertThat(addr.isInRange(IP_ADDR_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(ipAddress_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(IP_ADDR_172_16_192_99)).isFalse();
		assertThat(addr.isInRange(ipAddress_172_16_192_99)).isFalse();
	}

	@Test
	public void netmask24() {
		CIDR4Address addr = new CIDR4Address(IP_ADDR_192_168_64_33, 24);

		assertThat(addr).isNotNull();
		assertThat(addr.getAddress()).isEqualTo(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x00 });
		assertThat(addr.getNetmask()).isEqualTo(24);
		assertThat(addr.toString()).isEqualTo("192.168.64.0/24");
		assertThat(addr.isInRange(IP_ADDR_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(ipAddress_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(IP_ADDR_172_16_192_99)).isFalse();
		assertThat(addr.isInRange(ipAddress_172_16_192_99)).isFalse();
	}

	@Test
	public void netmask16() {
		CIDR4Address addr = new CIDR4Address(IP_ADDR_192_168_64_33, 16);

		assertThat(addr).isNotNull();
		assertThat(addr.getAddress()).isEqualTo(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 });
		assertThat(addr.getNetmask()).isEqualTo(16);
		assertThat(addr.toString()).isEqualTo("192.168.0.0/16");
		assertThat(addr.isInRange(IP_ADDR_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(ipAddress_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(IP_ADDR_172_16_192_99)).isFalse();
		assertThat(addr.isInRange(ipAddress_172_16_192_99)).isFalse();
	}

	@Test
	public void netmask8() {
		CIDR4Address addr = new CIDR4Address(IP_ADDR_192_168_64_33, 8);

		assertThat(addr).isNotNull();
		assertThat(addr.getAddress()).isEqualTo(new byte[] { (byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00 });
		assertThat(addr.getNetmask()).isEqualTo(8);
		assertThat(addr.toString()).isEqualTo("192.0.0.0/8");
		assertThat(addr.isInRange(IP_ADDR_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(ipAddress_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(IP_ADDR_172_16_192_99)).isFalse();
		assertThat(addr.isInRange(ipAddress_172_16_192_99)).isFalse();
	}

	@Test
	public void netmask0() {
		CIDR4Address addr = new CIDR4Address(IP_ADDR_192_168_64_33, 0);

		assertThat(addr).isNotNull();
		assertThat(addr.getAddress()).isEqualTo(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 });
		assertThat(addr.getNetmask()).isEqualTo(0);
		assertThat(addr.toString()).isEqualTo("0.0.0.0/0");
		assertThat(addr.isInRange(IP_ADDR_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(ipAddress_192_168_64_33)).isTrue();
		assertThat(addr.isInRange(IP_ADDR_172_16_192_99)).isTrue();
		assertThat(addr.isInRange(ipAddress_172_16_192_99)).isTrue();
	}
}
