/**
 * 
 */
package org.flowninja.collector.netflow9.impl;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.collector.common.netflow9.services.FlowStoreService;
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.netflow9.packet.InetSocketAddressPeerAddressMapper;
import org.flowninja.collector.netflow9.packet.PeerAddressMapper;
import org.osgi.service.blueprint.container.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author rainer
 *
 */
public class Netflow9CollectorHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Netflow9CollectorHandler.class);
	
	private List<FlowStoreService> flowStorers = new LinkedList<FlowStoreService>();
	private PeerAddressMapper peerAddressMapper = new InetSocketAddressPeerAddressMapper();

	/**
	 * @param flowStorers the flowStorers to set
	 */
	public void setFlowStorers(List<FlowStoreService> flowStorers) {
		this.flowStorers = flowStorers;
	}

	/**
	 * @param peerAddressMapper the peerAddressMapper to set
	 */
	public void setPeerAddressMapper(PeerAddressMapper peerAddressMapper) {
		this.peerAddressMapper = peerAddressMapper;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(FlowStoreService service : flowStorers) {
			try {
				service.activateFlowStorer();
			} catch(ServiceUnavailableException e) {
				logger.error("falied to activate flow storer", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		for(FlowStoreService service : flowStorers) {
			try {
				service.passivateFlowStorer();
			} catch(ServiceUnavailableException e) {
				logger.error("falied to activate flow storer", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		InetAddress peerAddress = peerAddressMapper.mapRemoteAddress(ctx.channel().remoteAddress());
		
		logger.info("received decoded flow packet from peer {}, msg type {}", peerAddress, 
				(msg != null ? msg.getClass().toString() : "<unknown>"));

		if(msg instanceof DataFlow) {
			DataFlow flow = (DataFlow)msg;
			
			logger.info("received data flow packet from peer {}", flow.getPeerAddress());
			
			for(FlowStoreService service : flowStorers) {
				try {
					service.storeDataFlow(flow);
				} catch(ServiceUnavailableException e) {
					logger.error("falied to store data flow", e);
				}
			}			
		} else if(msg instanceof OptionsFlow) {
			OptionsFlow flow = (OptionsFlow)msg;

			logger.info("received options flow packet from peer {}", flow.getPeerAddress());

			for(FlowStoreService service : flowStorers) {
				try {
					service.storeOptionsFlow(flow);
				} catch(ServiceUnavailableException e) {
					logger.error("falied to store options flow", e);
				}
			}			
			
		}
	}

	
}
