/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.flowninja.collector.netflow9.actors.Netflow9DatagramActor;
import org.flowninja.collector.netflow9.actors.NetworkServerShutdownMessage;
import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;
import org.flowninja.common.akka.SpringActorProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import groovy.util.logging.Slf4j;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author rainer
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class Netflow9DecodedDatagramHandler extends ChannelInboundHandlerAdapter {

	private final SpringActorProducer springActorProducer;

	private Map<InetAddress, ActorRef> datagramHandlerActors = new HashMap<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// intentionally left blank
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		this.datagramHandlerActors.entrySet().stream().map(e -> e.getValue())
				.forEach(a -> a.tell(new NetworkServerShutdownMessage(), null));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.
	 * channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		if (msg instanceof Netflow9DecodedDatagram) {
			final Netflow9DecodedDatagram datagram = (Netflow9DecodedDatagram) msg;

			findActorForPeer(datagram.getPeerAddress()).tell(
					Netflow9DatagramActor.ProcessNetflowDatagramRequest.builder().datagram(datagram).build(), null);
		}
	}

	private ActorRef findActorForPeer(final InetAddress peerAddress) {
		synchronized (datagramHandlerActors) {
			if (datagramHandlerActors.containsKey(peerAddress)) {
				return datagramHandlerActors.get(peerAddress);
			} else {
				final ActorRef actor = springActorProducer.createActor(Netflow9DatagramActor.class);

				datagramHandlerActors.put(peerAddress, actor);

				return actor;
			}
		}
	}

}
