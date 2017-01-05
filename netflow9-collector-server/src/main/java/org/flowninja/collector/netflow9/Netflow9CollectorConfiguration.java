package org.flowninja.collector.netflow9;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.flowninja.collector.netflow9.components.InetSocketAddressPeerAddressMapper;
import org.flowninja.collector.netflow9.components.PeerAddressMapper;

@Configuration
@ComponentScan(basePackageClasses=Netflow9CollectorConfiguration.class)
public class Netflow9CollectorConfiguration {

    @Bean
    public PeerAddressMapper peerAddressMapper() {
        return new InetSocketAddressPeerAddressMapper();
    }
}
