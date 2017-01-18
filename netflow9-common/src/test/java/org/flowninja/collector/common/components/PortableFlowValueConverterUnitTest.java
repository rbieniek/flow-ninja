package org.flowninja.collector.common.components;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import org.flowninja.collector.common.netflow9.components.PortableFlowValueConverter;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.collector.common.netflow9.types.PortableFlowValueRecord;

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
}
