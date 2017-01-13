package org.flowninja.common.kafka;

import java.net.InetAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbeddedKafkaBrokerProperties {
	private int portNumber;
	private InetAddress bindAddr;

	public String brokerAddress() {
		return bindAddr.getHostAddress() + ":" + Integer.toString(portNumber);
	}
}
