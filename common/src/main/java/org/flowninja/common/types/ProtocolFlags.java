package org.flowninja.common.types;

import java.util.List;

import org.flowninja.common.protocol.types.ForwardingStatus;
import org.flowninja.common.protocol.types.ICMPCode;
import org.flowninja.common.protocol.types.ICMPType;
import org.flowninja.common.protocol.types.IGMPType;
import org.flowninja.common.protocol.types.IPProtocol;
import org.flowninja.common.protocol.types.IPProtocolVersion;
import org.flowninja.common.protocol.types.IPv6OptionHeaders;
import org.flowninja.common.protocol.types.TCPFLags;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ProtocolFlags {

    private IPProtocolVersion ipProtocolVersion;
    private IPProtocol ipProtocol;
    private List<TCPFLags> tcpFlags;
    private List<IPv6OptionHeaders> ipv6OptionHeaders;
    private ICMPType icmpType;
    private ICMPCode icmpCode;
    private IGMPType igmpType;
    private ForwardingStatus forwardingStatus;
    private Integer minTtl;
    private Integer maxTtl;
    private Integer ipv4Ident;
    private Integer ipv6FlowLabel;
}
