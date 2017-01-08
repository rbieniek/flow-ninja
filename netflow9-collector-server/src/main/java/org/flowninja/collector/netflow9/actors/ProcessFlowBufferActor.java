package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;

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
public class ProcessFlowBufferActor extends UntypedActor {

    private final ActorRef templateDecoderActor;
    private final ActorRef templateRegistryActor;
    private final ActorRef unknownFlowsetProcessingActor;

    private FlowBuffer flowBuffer;
    private InetAddress peerAddress;

    @Override
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(ProcessFlowBufferRequest.class, m -> {
            this.flowBuffer = m.getFlowBuffer();
            this.peerAddress = m.getPeerAddress();

            templateRegistryActor.tell(
                    TemplateRegistryActor.LookupTemplateRequest.builder().flowsetId(flowBuffer.getFlowSetId()).build(),
                    getSelf());
        }).onType(TemplateRegistryActor.LookupDataTemplateResponse.class, m -> {
            templateDecoderActor.tell(
                    TemplateDecoderActor.DataTemplateDecoderRequest.builder()
                            .flowBuffer(flowBuffer)
                            .dataTemplate(m.getDataTemplate())
                            .peerAddress(peerAddress)
                            .build(),
                    getSelf());

            getContext().stop(getSelf());
        }).onType(TemplateRegistryActor.LookupOptionsTemplateResponse.class, m -> {
            templateDecoderActor.tell(
                    TemplateDecoderActor.OptionsTemplateDecoderRequest.builder()
                            .flowBuffer(flowBuffer)
                            .optionsTemplate(m.getOptionsTemplate())
                            .peerAddress(peerAddress)
                            .build(),
                    getSelf());
            getContext().stop(getSelf());
        }).onType(TemplateRegistryActor.UnknownFlowsetResponse.class, m -> {
            unknownFlowsetProcessingActor.tell(
                    UnknownFlowsetProcessingActor.StoreUnknownFlowBufferRequest.builder()
                            .flowBuffer(flowBuffer)
                            .peerAddress(peerAddress)
                            .build(),
                    getSelf());
            getContext().stop(getSelf());
        }).unhandled(m -> unhandled(m));
    }

    @Builder
    @Getter
    public static class ProcessFlowBufferRequest {

        private InetAddress peerAddress;
        private FlowBuffer flowBuffer;
    }
}
