/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netwflow9 datagram packet decoder implementation. 
 * 
 * This decoder receives a byte-encoded netflow 9 packet
 * and sents a decoded packet object upstream.
 * 
 * The packet decoder temporarily stores received flow packet if a matching template record has not been received yet
 * 
 * @author rainer
 *
 */
public class Netflow9PacketDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(Netflow9PacketDecoder.class);
	
	private PeerRegistry peerRegistry;
	
	/**
	 * @param peerFlowRegistry the peerFlowRegistry to set
	 */
	public void setPeerRegistry(PeerRegistry peerFlowRegistry) {
		this.peerRegistry = peerFlowRegistry;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is active");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is inactive");
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		InetAddress peerAddress = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress();
		
		logger.info("received flow packet from peer {}", peerAddress);
		
		FlowRegistry flowRegistry = peerRegistry.registryForPeerAddress(peerAddress);
	}

}
