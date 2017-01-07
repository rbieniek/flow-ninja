package org.flowninja.collector.common.netflow9.actors;

import org.flowninja.collector.common.netflow9.types.DataFlow;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataFlowMessage {
	private DataFlow dataFlow;
}
