package org.flowninja.collector.common.components;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import org.flowninja.collector.common.netflow9.components.PortableFlowValueConverter;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.FlowValueRecord;
import org.flowninja.common.types.PortableFlowValueRecord;

import static org.assertj.core.api.Assertions.assertThat;

public class PortableFlowValueConverterUnitTest {

    private PortableFlowValueConverter conv;

    @Before
    public void before() {
        conv = new PortableFlowValueConverter();
    }

    @Test
    public void shouldConvertApplicationName() {
        final PortableFlowValueRecord r = conv.convertFlowValueRecord(
                Arrays.asList(FlowValueRecord.builder().type(FieldType.APPLICATION_NAME).value("foo").build()));

        assertThat(r).isNotNull();
        assertThat(r.getFlowStatistics()).isNotNull();
        assertThat(r.getFlowStatistics().getApplicationDescription()).isNull();
        assertThat(r.getFlowStatistics().getApplicationName()).isEqualTo("foo");
        assertThat(r.getFlowStatistics().getApplicationTag()).isNull();
        assertThat(r.getFlowStatistics().getEngineId()).isNull();
        assertThat(r.getFlowStatistics().getEngineType()).isNull();
        assertThat(r.getFlowStatistics().getFirstSwitched()).isNull();
        assertThat(r.getFlowStatistics().getFlowActiveTimeout()).isNull();
        assertThat(r.getFlowStatistics().getFlowClass()).isNull();
        assertThat(r.getFlowStatistics().getFlowDirection()).isNull();
        assertThat(r.getFlowStatistics().getFlowInactiveTimeout()).isNull();
        assertThat(r.getFlowStatistics().getFlows()).isNull();
        assertThat(r.getFlowStatistics().getFlowSamplerId()).isNull();
        assertThat(r.getFlowStatistics().getFlowSamplerRandomInterval()).isNull();
        assertThat(r.getFlowStatistics().getLastSwitched()).isNull();
        assertThat(r.getFlowStatistics().getMaxPktLength()).isNull();
        assertThat(r.getFlowStatistics().getMinPktLength()).isNull();
        assertThat(r.getFlowStatistics().getSamplerName()).isNull();
        assertThat(r.getFlowStatistics().getSamplingAlgorithm()).isNull();
        assertThat(r.getFlowStatistics().getSamplingInterval()).isNull();
        assertThat(r.getFlowStatistics().getTotalFlowsExported()).isNull();

        assertThat(r.getBgpNextHop()).isNull();
        assertThat(r.getDstAddress()).isNull();
        assertThat(r.getNextHop()).isNull();
        assertThat(r.getPayloadCounters()).isNull();
        assertThat(r.getProtocolFlags()).isNull();
        assertThat(r.getSrcAddress()).isNull();
        assertThat(r.getTypeOfService()).isNull();
    }
}
