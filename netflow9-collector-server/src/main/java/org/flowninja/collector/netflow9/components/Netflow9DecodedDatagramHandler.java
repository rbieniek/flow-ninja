/**
 *
 */
package org.flowninja.collector.netflow9.components;

import org.springframework.stereotype.Component;

import groovy.util.logging.Slf4j;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author rainer
 *
 */
@Slf4j
@Component
public class Netflow9DecodedDatagramHandler extends ChannelInboundHandlerAdapter {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
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

		}
	}

}
