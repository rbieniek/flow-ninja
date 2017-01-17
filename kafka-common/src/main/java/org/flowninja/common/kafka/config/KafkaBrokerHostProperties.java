package org.flowninja.common.kafka.config;

import java.net.InetAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaBrokerHostProperties {
	private int portNumber;
	private InetAddress host;

	public String brokerAddress() {
		return host.getHostAddress() + ":" + Integer.toString(portNumber);
	}
}
