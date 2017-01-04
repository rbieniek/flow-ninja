/**
 * 
 */
package org.flowninja.collector.common.types;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;
/**
 * @author rainer
 *
 */
public class IntCounterTest {
	
	@Test
	public void octetOneTest() {
		IntCounter counter = new IntCounter(new byte[] { (byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00 });
		Integer value = new Integer(0xc0000000);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetTwoTest() {
		IntCounter counter = new IntCounter(new byte[] { (byte)0x00, (byte)0xc0, (byte)0x00, (byte)0x00 });
		Integer value = new Integer(0x00c00000);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetThreeTest() {
		IntCounter counter = new IntCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0xc0, (byte)0x00 });
		Integer value = new Integer(0x0000c000);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetFourTest() {
		IntCounter counter = new IntCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xc0 });
		Integer value = new Integer(0x000000c0);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
}
