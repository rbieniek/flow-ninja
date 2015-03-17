/**
 * 
 */
package org.flowninja.collector.netflow9.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.flowninja.collector.netflow9.Netflow9CollectorService;
import org.flowninja.collector.netflow9.packet.Netflow9PacketDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class Netflow9CollectorServiceImpl implements Netflow9CollectorService {
	private static final Logger logger = LoggerFactory.getLogger(Netflow9CollectorServiceImpl.class);
	
	private String serviceId;
	private AtomicReference<InetAddress> bindAddress = new AtomicReference<InetAddress>();
	private AtomicInteger bindPort = new AtomicInteger(-1);
	private Netflow9CollectorHandler netflowCollector;
	private AtomicBoolean canRun = new AtomicBoolean(false);
	private AtomicBoolean serverRunning = new AtomicBoolean(false);
	private EventLoopGroup group = null;
	private Channel serverChannel;
	
	public void init() {
		logger.info("starting Netflow9 collector service, serviceId=\"{}\"", serviceId);

		canRun.set(true);
		
		startServer();
	}
	
	public void destroy() {
		logger.info("stopping Netflow9 collector service, serviceId=\"{}\"", serviceId);

		canRun.set(false);
		
		stopServer();
	}
	
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	/**
	 * @return the bindAddress
	 */
	public InetAddress getBindAddress() {
		return bindAddress.get();
	}
	
	/**
	 * @param bindAddress the bindAddress to set
	 */
	public void setBindAddress(InetAddress bindAddress) {
		this.bindAddress.set(bindAddress);
		
		if(canRun.get()) {
			stopServer();

			startServer();
		}
	}
	
	/**
	 * @return the bindPort
	 */
	public int getBindPort() {
		return bindPort.get();
	}
	
	/**
	 * @param bindPort the bindPort to set
	 */
	public void setBindPort(int bindPort) {
		this.bindPort.set(bindPort);
		
		if(canRun.get()) {
			stopServer();
			
			startServer();
		}
	}
	
	/**
	 * @param netflowCollector the netflowCollector to set
	 */
	public void setNetflowCollector(Netflow9CollectorHandler netflowCollector) {
		this.netflowCollector = netflowCollector;
	}

	private void stopServer() {
		logger.info("stopping netflow v9 collector server");

		if(serverRunning.get()) {
			serverChannel.close().addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					serverChannel = null;
					group.shutdownGracefully().addListener(new FutureListener<Object>() {

						@Override
						public void operationComplete(Future<Object> future) throws Exception {
							group = null;
							
							serverRunning.set(false);
						}
					});
					
				}
			});
		}
	}

	private void startServer() {
		logger.info("starting netflow v9 collector server");
		
		if(!serverRunning.get()) {
			group = new NioEventLoopGroup();
			
			Bootstrap b = new Bootstrap();
			
			b.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.option(ChannelOption.SO_REUSEADDR, true)
				.handler(new Netflow9PacketDecoder())
				.handler(netflowCollector);
			
			b.bind(this.bindAddress.get(), this.bindPort.get()).addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					serverChannel = future.channel();
					serverRunning.set(true);
				}
			});
		}
	}


}
