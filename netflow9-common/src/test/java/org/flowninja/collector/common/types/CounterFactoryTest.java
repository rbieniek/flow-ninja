/**
 *
 */
package org.flowninja.collector.common.types;

import java.math.BigInteger;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author rainer
 *
 */
public class CounterFactoryTest {

    @Test
    public void fourOctetTest() {
        final Counter counter = CounterFactory.decode(new byte[] { (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
        final Integer value = Integer.valueOf(0xc0000000);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void eightOctetTest() {
        final Counter counter = CounterFactory.decode(
                new byte[] {
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0xc000000000000000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void twelveOctetTest() {
        final Counter counter = CounterFactory.decode(
                new byte[] {
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final BigInteger value = new BigInteger(
                new byte[] {
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }
}
