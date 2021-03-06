/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.Set;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.*;

/**
 * @author rainer
 *
 */
public class IPv6OptionHeadersTest {

	@Test
	public void testEmpty() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0000);
		
		assertThat(headers).isEmpty();
	}

	@Test
	public void testBit0() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0001);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.RESERVED);
	}

	@Test
	public void testBit1() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0002);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.FRA_X);
	}
	
	@Test
	public void testBit2() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0004);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.RH);
	}
	
	@Test
	public void testBit3() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0008);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.FRA_0);
	}
	
	@Test
	public void testBit4() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0010);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.UNK);
	}
	
	@Test
	public void testBit5() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0020);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.RESERVED);
	}
	
	@Test
	public void testBit6() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0040);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.HOP);
	}
	
	@Test
	public void testBit7() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0080);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.DST);
	}
	
	@Test
	public void testBit8() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0100);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.PAY);
	}
	
	@Test
	public void testBit9() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0200);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.AH);
	}

	@Test
	public void testBit10() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0400);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.ESP);
	}
	
	@Test
	public void testBit11() {
		Set<IPv6OptionHeaders> headers = IPv6OptionHeaders.fromCode(0x0800);
		
		assertThat(headers).containsOnly(IPv6OptionHeaders.RESERVED);
	}
}
