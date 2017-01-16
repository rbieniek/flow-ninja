/**
 *
 */
package org.flowninja.collector.common.types;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author rainer
 *
 */
public class IntCounterTest {

    @Test
    public void octetOneTest() {
        final IntCounter counter = new IntCounter(new byte[] { (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
        final Integer value = Integer.valueOf(0xc0000000);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetTwoTest() {
        final IntCounter counter = new IntCounter(new byte[] { (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00 });
        final Integer value = Integer.valueOf(0x00c00000);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetThreeTest() {
        final IntCounter counter = new IntCounter(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0x00 });
        final Integer value = Integer.valueOf(0x0000c000);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetFourTest() {
        final IntCounter counter = new IntCounter(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0 });
        final Integer value = Integer.valueOf(0x000000c0);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }
}
