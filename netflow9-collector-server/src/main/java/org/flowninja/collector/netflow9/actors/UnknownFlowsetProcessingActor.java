package org.flowninja.collector.netflow9.actors;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AkkaComponent
@RequiredArgsConstructor
public class UnknownFlowsetProcessingActor extends UntypedActor {

	private final ActorRef templateDecoderActor;

	private Map<Integer, List<FlowBuffer>> flowBufferMap = new HashMap<>();

	@Override
	public void onReceive(final Object message) throws Throwable {
		ActorUtils.withMessage(message)
				.onType(StoreUnknownFlowBufferRequest.class, m -> putFlowBuffer(m.getFlowBuffer()))
				.onType(TemplateRegistryActor.DataTemplateAvailableRequest.class, m -> {
					getFlowBuffers(m.getDataTemplate().getFlowsetId()).forEach(fb -> {
						templateDecoderActor.tell(TemplateDecoderActor.DataTemplateDecoderRequest.builder()
								.dataTemplate(m.getDataTemplate()).flowBuffer(fb).build(), getSelf());
					});
				}).onType(TemplateRegistryActor.OptionsTemplateAvailableRequest.class, m -> {
					getFlowBuffers(m.getOptionsTemplate().getFlowsetId()).forEach(fb -> {
						templateDecoderActor.tell(TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
								.optionsTemplate(m.getOptionsTemplate()).flowBuffer(fb).build(), getSelf());
					});
				}).unhandled(m -> unhandled(m));
	}

	private void putFlowBuffer(final FlowBuffer flowBuffer) {
		if (!flowBufferMap.containsKey(flowBuffer.getFlowSetId())) {
			flowBufferMap.put(flowBuffer.getFlowSetId(), new LinkedList<>());
		}

		flowBufferMap.get(flowBuffer.getFlowSetId()).add(flowBuffer);
	}

	private List<FlowBuffer> getFlowBuffers(final int flowsetId) {
		if (!flowBufferMap.containsKey(flowsetId)) {
			return Collections.emptyList();
		}

		return flowBufferMap.get(flowsetId);
	}

	@Builder
	@Getter
	public static class StoreUnknownFlowBufferRequest {
		private FlowBuffer flowBuffer;
	}
}
