package org.flowninja.collector.netflow9.actors;

import java.net.InetAddress;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collector.common.netflow9.actors.TemplateDecodingFailureMessage;
import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.Template;
import org.flowninja.collector.netflow9.components.TemplateDecoder;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;
import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@AkkaComponent
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class TemplateDecoderActor extends UntypedActor {

	private final TemplateDecoder templateDecoder;
	private final SinkActorsProvider sinkActorsProvider;

	@Override
	public void onReceive(final Object message) throws Throwable {
		ActorUtils.withMessage(message).onType(DataTemplateDecoderRequest.class, m -> decodeDataTemplate(m))
				.onType(OptionsTemplateDecoderRequest.class, m -> decodeOptionsTemplate(m))
				.unhandled(m -> unhandled(m));
	}

	private void decodeDataTemplate(final DataTemplateDecoderRequest request) {
		try {
			templateDecoder
					.decodeDataTemplate(request.getPeerAddress(), request.getFlowBuffer(), request.getDataTemplate())
					.forEach(df -> {
						sinkActorsProvider.getDataFlowActor().tell(DataFlowMessage.builder().dataFlow(df).build(),
								getSelf());
					});
		} catch (Throwable t) {
			log.warn("Failed to decode template, request {}", request, t);

			sinkActorsProvider.getDecodingFailureActor().tell(createFailureMessage(null, request.getDataTemplate(),
					request.getPeerAddress(), request.getFlowBuffer(), t), getSelf());
		}
	}

	private void decodeOptionsTemplate(final OptionsTemplateDecoderRequest request) {
		try {
			templateDecoder.decodeOptionsTemplate(request.getPeerAddress(), request.getFlowBuffer(),
					request.getOptionsTemplate()).forEach(of -> {
						sinkActorsProvider.getDataFlowActor().tell(OptionsFlowMessage.builder().optionsFlow(of).build(),
								getSelf());
					});
		} catch (Throwable t) {
			log.warn("Failed to decode template, request {}", request, t);

			sinkActorsProvider.getDecodingFailureActor().tell(createFailureMessage(request.getOptionsTemplate(), null,
					request.getPeerAddress(), request.getFlowBuffer(), t), getSelf());
		}

	}

	private TemplateDecodingFailureMessage createFailureMessage(final OptionsTemplate optionsTemplate,
			final Template template, final InetAddress peerAddres, final FlowBuffer flowbuffer,
			final Throwable reason) {
		final ByteBuf buf = flowbuffer.getBuffer();

		buf.resetReaderIndex();

		byte[] memoryBuffer = new byte[buf.readableBytes()];

		buf.readBytes(memoryBuffer);

		return TemplateDecodingFailureMessage.builder().dataTemplate(template).optionsTemplate(optionsTemplate)
				.flowSetId(flowbuffer.getFlowSetId()).header(flowbuffer.getHeader()).payload(memoryBuffer)
				.reason(reason).build();
	}

	@Builder
	@Getter
	@ToString
	public static class DataTemplateDecoderRequest {
		private InetAddress peerAddress;
		private Template dataTemplate;
		private FlowBuffer flowBuffer;
	}

	@Builder
	@Getter
	@ToString
	public static class OptionsTemplateDecoderRequest {
		private InetAddress peerAddress;
		private OptionsTemplate optionsTemplate;
		private FlowBuffer flowBuffer;
	}
}
