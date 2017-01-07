package org.flowninja.collector.netflow9;

import java.net.InetAddress;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "netflow9.server")
@Getter
@Setter
public class Netflow9CollectorProperties {
	@Min(value = 1025)
	@Max(value = 65535)
	private int port;

	private InetAddress address;
}
