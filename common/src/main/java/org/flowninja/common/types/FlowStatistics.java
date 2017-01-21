package org.flowninja.common.types;

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
    private Integer flowSamplerRandomInterval;
    private Long firstSwitched;
    private Long lastSwitched;
}
