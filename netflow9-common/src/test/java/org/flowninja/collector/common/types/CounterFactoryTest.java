/**
 * 
 */
package org.flowninja.collector.common.types;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.Test;

/**
 * @author rainer
 *
 */
public class CounterFactoryTest {
	@Test
	public void fourOctetTest() {
		Counter counter = CounterFactory.decode(new byte[] { (byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00 });
		Integer value = new Integer(0xc0000000);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}

	@Test
	public void eightOctetTest() {
		Counter counter = CounterFactory.decode(new byte[] {(byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
		Long value = new Long(0xc000000000000000L);

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}


	@Test
	public void twelveOctetTest() {
		Counter counter = CounterFactory.decode(new byte[] {(byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
		BigInteger value = new BigInteger(new byte[] {(byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});

		assertThat(counter.value()).isEqualTo(value);
		assertThat(counter.printableValue()).isEqualTo(value.toString());
	}
}
