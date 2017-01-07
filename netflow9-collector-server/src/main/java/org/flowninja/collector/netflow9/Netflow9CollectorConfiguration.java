package org.flowninja.collector.netflow9;

import org.flowninja.collector.netflow9.components.InetSocketAddressPeerAddressMapper;
import org.flowninja.collector.netflow9.components.PeerAddressMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Netflow9CollectorConfiguration.class)
public class Netflow9CollectorConfiguration {

	@Bean
	public PeerAddressMapper peerAddressMapper() {
		return new InetSocketAddressPeerAddressMapper();
	}
}
