package org.flowninja.common.kafka;

import java.net.InetAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbeddedZookeeperProperties {
    private int portNumber;
    private InetAddress bindAddr;
}
