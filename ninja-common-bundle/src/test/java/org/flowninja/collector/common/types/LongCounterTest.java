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
public class LongCounterTest {
	
	@Test
	public void octetOneTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
		Long value = new Long(0xc000000000000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetTwoTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0xc0, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 });
		Long value = new Long(0x00c0000000000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetThreeTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0xc0, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 });
		Long value = new Long(0x0000c00000000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetFourTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xc0,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 });
		Long value = new Long(0x000000c000000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}

	@Test
	public void octetFiveTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00});
		Long value = new Long(0xc0000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetSixTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0xc0, (byte)0x00, (byte)0x00 });
		Long value = new Long(0xc00000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetSevenTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0xc0, (byte)0x00 });
		Long value = new Long(0xc000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
	
	@Test
	public void octetEightTest() {
		LongCounter counter = new LongCounter(new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0xc0 });
		Long value = new Long(0xc0L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
}
