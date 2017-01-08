package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

@AkkaComponent
@RequiredArgsConstructor
public class UnknownFlowsetProcessingActor extends UntypedActor {

    private final ActorRef templateDecoderActor;

    private Map<Integer, List<Pair<InetAddress, FlowBuffer>>> flowBufferMap = new HashMap<>();

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message)
                .onType(StoreUnknownFlowBufferRequest.class, m -> putFlowBuffer(m.getPeerAddress(), m.getFlowBuffer()))
                .onType(TemplateRegistryActor.DataTemplateAvailableRequest.class, m -> {
                    getFlowBuffers(m.getDataTemplate().getFlowsetId()).forEach(p -> {
                        templateDecoderActor.tell(
                                TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                                        .dataTemplate(m.getDataTemplate())
                                        .peerAddress(p.getLeft())
                                        .flowBuffer(p.getRight())
                                        .build(),
                                getSelf());
                    });
                })
                .onType(TemplateRegistryActor.OptionsTemplateAvailableRequest.class, m -> {
                    getFlowBuffers(m.getOptionsTemplate().getFlowsetId()).forEach(p -> {
                        templateDecoderActor.tell(
                                TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                                        .optionsTemplate(m.getOptionsTemplate())
                                        .peerAddress(p.getLeft())
                                        .flowBuffer(p.getRight())
                                        .build(),
                                getSelf());
                    });
                })
                .unhandled(m -> unhandled(m));
    }

    private void putFlowBuffer(final InetAddress peerAddress, final FlowBuffer flowBuffer) {
        if (!flowBufferMap.containsKey(flowBuffer.getFlowSetId())) {
            flowBufferMap.put(flowBuffer.getFlowSetId(), new LinkedList<>());
        }

        flowBufferMap.get(flowBuffer.getFlowSetId()).add(Pair.of(peerAddress, flowBuffer));
    }

    private List<Pair<InetAddress, FlowBuffer>> getFlowBuffers(final int flowsetId) {
        if (!flowBufferMap.containsKey(flowsetId)) {
            return Collections.emptyList();
        }

        return flowBufferMap.get(flowsetId);
    }

    @Builder
    @Getter
    public static class StoreUnknownFlowBufferRequest {

        private InetAddress peerAddress;
        private FlowBuffer flowBuffer;
    }
}
