/**
 * 
 */
package org.flowninja.rspl.definitions;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class CIDR4AddressRangeParserTest {
	
	private CIDR4Address targetAddress;
	
	@Before
	public void before() {
		targetAddress = new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x00}, 24);
	}
	
	@Test
	public void rangeWithSpaces() {
		CIDR4Address  addr = CIDR4AddressRangeParser.parse("80.239.186.0 - 80.239.186.255");
		
		assertThat(addr).isEqualTo(targetAddress);
	}

	@Test
	public void rangeWithoutSpaces() {
		CIDR4Address  addr = CIDR4AddressRangeParser.parse("80.239.186.0-80.239.186.255");
		
		assertThat(addr).isEqualTo(targetAddress);
	}

	@Test
	public void rangeEqual() {
		CIDR4Address  addr = CIDR4AddressRangeParser.parse("80.239.186.255 - 80.239.186.255");
		
		assertThat(addr).isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0xff}, 32));
	}

	@Test(expected=IllegalArgumentException.class)
	public void shortStartRange() {
		CIDR4AddressRangeParser.parse("80.239.186 - 80.239.186.255");
	}

	@Test(expected=IllegalArgumentException.class)
	public void shortStartRangeDot() {
		CIDR4AddressRangeParser.parse("80.239.186. - 80.239.186.255");
	}

	@Test(expected=IllegalArgumentException.class)
	public void longStartRange() {
		CIDR4AddressRangeParser.parse("80.239.186.0.1 - 80.239.186.255");
	}

	@Test(expected=IllegalArgumentException.class)
	public void startMissing() {
		CIDR4AddressRangeParser.parse(" - 80.239.186.255");
	}

	@Test(expected=IllegalArgumentException.class)
	public void endMissing() {
		CIDR4AddressRangeParser.parse("80.239.186.0 - ");
	}

	@Test(expected=IllegalArgumentException.class)
	public void noRange() {
		CIDR4AddressRangeParser.parse("80.239.186.0 80.239.186.255");
	}

	@Test
	public void rangeNet4() {
		assertThat(CIDR4AddressRangeParser.parse("80.239.186.0 - 80.239.186.15"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x00}, 28));
		assertThat(CIDR4AddressRangeParser.parse("80.239.186.64 - 80.239.186.79"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x40}, 28));
	}

	@Test
	public void rangeNet6() {
		assertThat(CIDR4AddressRangeParser.parse("80.239.186.0 - 80.239.186.63"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x00}, 26));
		assertThat(CIDR4AddressRangeParser.parse("80.239.186.64 - 80.239.186.126"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x40}, 26));
	}

	@Test
	public void rangeNet8() {
		assertThat(CIDR4AddressRangeParser.parse("80.239.186.0 - 80.239.186.255"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x00}, 24));
	}

	@Test
	public void rangeNet10() {
		assertThat(CIDR4AddressRangeParser.parse("80.239.184.0 - 80.239.187.255"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xb8, (byte)0x00}, 22));
	}

	@Test
	public void rangeNet12() {
		assertThat(CIDR4AddressRangeParser.parse("80.239.176.0 - 80.239.187.255"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0xef, (byte)0xb0, (byte)0x00}, 20));
	}
	
	@Test
	public void rangeNet24() {
		assertThat(CIDR4AddressRangeParser.parse("80.0.0.0 - 80.255.255.255"))
			.isEqualTo(new CIDR4Address(new byte[] { (byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00}, 8));
	}
}
