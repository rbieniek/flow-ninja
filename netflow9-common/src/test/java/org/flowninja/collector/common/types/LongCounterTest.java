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
public class LongCounterTest {

    @Test
    public void octetOneTest() {
        final LongCounter counter = new LongCounter(
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
    public void octetTwoTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0x00c0000000000000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetThreeTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0x0000c00000000000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetFourTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0x000000c000000000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetFiveTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0xc0000000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetSixTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0xc00000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetSevenTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0,
                        (byte) 0x00 });
        final Long value = Long.valueOf(0xc000L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }

    @Test
    public void octetEightTest() {
        final LongCounter counter = new LongCounter(
                new byte[] {
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0x00,
                        (byte) 0xc0 });
        final Long value = Long.valueOf(0xc0L);

        assertThat(counter.value()).isEqualTo(value);
        assertThat(counter.printableValue()).isEqualTo(value.toString());
    }
}
