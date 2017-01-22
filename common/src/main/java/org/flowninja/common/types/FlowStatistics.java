package org.flowninja.common.types;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class FlowStatistics {

    private FlowDirection flowDirection;
    private Integer minPktLength;
    private Integer maxPktLength;
    private Integer flowActiveTimeout;
    private Integer flowInactiveTimeout;
    private Integer totalFlowsExported;
    private Integer flowClass;
    private Integer samplingInterval;
    private Long flowSamplerRandomInterval;
    private Long firstSwitched;
    private Long lastSwitched;
    private String applicationName;
    private String applicationDescription;
    private String applicationTag;
    private Integer engineId;
    private EngineType engineType;
    private Integer flowSamplerId;
    private SamplingAlgorithm samplingAlgorithm;
    private BigInteger flows;
    private String samplerName;
}
