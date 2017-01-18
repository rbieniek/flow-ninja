package org.flowninja.collector.common.components;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import org.flowninja.collector.common.netflow9.components.PortableFlowValueConverter;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.netflow9.types.PortableFlowValueRecord;
import org.flowninja.collector.common.protocol.types.ICMPCode;
import org.flowninja.collector.common.protocol.types.ICMPType;
import org.flowninja.collector.common.protocol.types.ICMPTypeCode;
import org.flowninja.collector.common.protocol.types.TCPFLags;
import org.flowninja.collector.common.types.EncodedData;

import static org.assertj.core.api.Assertions.assertThat;

public class PortableFlowValueConverterUnitTest {

    private PortableFlowValueConverter conv;

    @Before
    public void before() {
        conv = new PortableFlowValueConverter();
    }

    @Test
    public void shouldConvertString() {
        final PortableFlowValueRecord r = conv
                .convertFlowValueRecord(FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value("foo").build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.lang.String");
        assertThat(r.getStringValue()).isEqualTo("foo");
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();

    }

    @Test
    public void shouldConvertInteger() {
        final PortableFlowValueRecord r = conv
                .convertFlowValueRecord(FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(1).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.lang.Integer");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isEqualTo(BigInteger.ONE);

    }

    @Test
    public void shouldConvertLong() {
        final PortableFlowValueRecord r = conv
                .convertFlowValueRecord(FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(1L).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.lang.Long");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isEqualTo(BigInteger.ONE);

    }

    @Test
    public void shouldConvertInet4Address() {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(Inet4Address.getLoopbackAddress()).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.net.Inet4Address");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isEqualTo("127.0.0.1");
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();

    }

    @Test
    public void shouldConvertInet6Address() throws UnknownHostException {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                FlowValueRecord.builder()
                        .type(FieldType.APPLICATION_NAME)
                        .value(Inet6Address.getByAddress(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }))
                        .build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.net.Inet6Address");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isEqualTo("0:0:0:0:0:0:0:1");
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();

    }

    @Test
    public void shouldConvertEncodedValue() throws UnknownHostException {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                FlowValueRecord.builder()
                        .type(FieldType.APPLICATION_NAME)
                        .value(EncodedData.builder().data(new byte[] { 1, 2, 3, 4 }).encodedData("AQIDBAA==").build())
                        .build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("org.flowninja.collector.common.types.EncodedData");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isEqualTo("AQIDBAA==");
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();

    }

    @Test
    public void shouldConvertByteArray() throws UnknownHostException {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(new byte[] { 1, 2, 3, 4 }).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.lang.reflect.Array");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isEqualTo("AQIDBA==");
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();

    }

    @Test
    public void shouldConvertEnum() throws UnknownHostException {
        final PortableFlowValueRecord r = conv
                .convertFlowValueRecord(FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(TCPFLags.ACK).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("org.flowninja.collector.common.protocol.types.TCPFLags");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).isNull();
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isEqualTo("ACK");
        assertThat(r.getNumberValue()).isNull();
    }

    @Test
    public void shouldConvertCollection() throws UnknownHostException {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value(Arrays.asList(1, 2)).build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.util.Collection");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).containsExactly(
                PortableFlowValueRecord.builder().objectClass("java.lang.Integer").numberValue(BigInteger.valueOf(1L)).build(),
                PortableFlowValueRecord.builder().objectClass("java.lang.Integer").numberValue(BigInteger.valueOf(2L)).build());
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();
    }

    @Test
    public void shouldConvertICMPTypeCode() throws UnknownHostException {
        final PortableFlowValueRecord r = conv
                .convertFlowValueRecord(FlowValueRecord.builder()
                        .type(FieldType.APPLICATION_NAME)
                        .value(
                                ICMPTypeCode.builder()
                                        .code(ICMPCode.ALTERNATE_ADDRESS_FOR_HOST)
                                        .type(ICMPType.ALTERNATE_HOST_ADDRESS)
                                        .build())
                        .build());

        assertThat(r).isNotNull();
        assertThat(r.getType()).isEqualTo("APPLICATION_NAME");
        assertThat(r.getObjectClass()).isEqualTo("java.util.Collection");
        assertThat(r.getStringValue()).isNull();
        assertThat(r.getAddrValue()).isNull();
        assertThat(r.getCollectionValue()).containsExactly(
                PortableFlowValueRecord.builder()
                        .objectClass("org.flowninja.collector.common.protocol.types.ICMPType")
                        .enumValue("ALTERNATE_HOST_ADDRESS")
                        .build(),
                PortableFlowValueRecord.builder()
                        .objectClass("org.flowninja.collector.common.protocol.types.ICMPCode")
                        .enumValue("ALTERNATE_ADDRESS_FOR_HOST")
                        .build());
        assertThat(r.getEncodedValue()).isNull();
        assertThat(r.getEnumValue()).isNull();
        assertThat(r.getNumberValue()).isNull();
    }

}
