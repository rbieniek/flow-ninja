/**
 *
 */
package org.flowninja.collector.netflow9;

import java.net.InetAddress;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.components.SinkActorsProvider;
import org.flowninja.collector.netflow9.actors.NetworkServerStartupMessage;
import org.flowninja.collector.netflow9.components.Netflow9DatagramDecoder;
import org.flowninja.collector.netflow9.components.Netflow9DecodedDatagramHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rainer
 *
 */
@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class Netflow9CollectorServer implements InitializingBean, DisposableBean {

    private final Netflow9CollectorProperties collectorProperties;
    private final Netflow9DatagramDecoder netflowDatagramDecoder;
    private final Netflow9DecodedDatagramHandler netflowDecodedDatagramHandler;
    private final SinkActorsProvider sinkActorsProvider;

    private EventLoopGroup group = null;
    private Channel serverChannel;

    @Override
    public void destroy() throws Exception {
        serverChannel.close().addListener(future -> {
            serverChannel = null;
            group.shutdownGracefully().addListener(future1 -> {
                group = null;

            });

        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("starting netflow v9 collector server on {}", collectorProperties);

        group = new NioEventLoopGroup();

        final Bootstrap b = new Bootstrap();

        b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(final Channel ch) throws Exception {
                        ch.pipeline().addLast(netflowDatagramDecoder).addLast(netflowDecodedDatagramHandler);
                    }
                });

        b.bind(
                collectorProperties.getAddress() != null ? collectorProperties.getAddress() : InetAddress.getLocalHost(),
                collectorProperties.getPort()).addListener(new OpenListener());
    }

    private class OpenListener implements ChannelFutureListener {

        @Override
        public void operationComplete(final ChannelFuture future) throws Exception {
            log.info("Server startup complete on {}", collectorProperties);

            serverChannel = future.channel();

            sinkActorsProvider.getServerEventActor().tell(new NetworkServerStartupMessage(), null);

        }
    }

}
