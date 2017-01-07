package org.flowninja.collector.common.netflow9.actors;

import org.flowninja.collector.common.netflow9.types.OptionsFlow;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionsFlowMessage {
	private OptionsFlow optionsFlow;
}
