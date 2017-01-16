/**
 *
 */
package org.flowninja.collector.common.protocol.types;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rainer
 *
 */
public enum TCPFLags {
    ZERO,
    FUTURE_USE,
    NS,
    CWR,
    ECE,
    URG,
    ACK,
    PSH,
    RST,
    SYN,
    FIN;

    @SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
    public static Set<TCPFLags> fromCode(final int code) {
        final Set<TCPFLags> flags = new HashSet<>();

        if ((code & 0x0001) != 0) {
            flags.add(TCPFLags.FIN);
        }
        if ((code & 0x0002) != 0) {
            flags.add(TCPFLags.SYN);
        }
        if ((code & 0x0004) != 0) {
            flags.add(TCPFLags.RST);
        }
        if ((code & 0x0008) != 0) {
            flags.add(TCPFLags.PSH);
        }
        if ((code & 0x0010) != 0) {
            flags.add(TCPFLags.ACK);
        }
        if ((code & 0x0020) != 0) {
            flags.add(TCPFLags.URG);
        }
        if ((code & 0x0040) != 0) {
            flags.add(TCPFLags.ECE);
        }
        if ((code & 0x0080) != 0) {
            flags.add(TCPFLags.CWR);
        }
        if ((code & 0x0100) != 0) {
            flags.add(TCPFLags.NS);
        }
        if ((code & 0x0200) != 0) {
            flags.add(TCPFLags.FUTURE_USE);
        }
        if ((code & 0x0400) != 0) {
            flags.add(TCPFLags.FUTURE_USE);
        }
        if ((code & 0x0800) != 0) {
            flags.add(TCPFLags.FUTURE_USE);
        }
        if ((code & 0x1000) != 0) {
            flags.add(TCPFLags.ZERO);
        }
        if ((code & 0x2000) != 0) {
            flags.add(TCPFLags.ZERO);
        }
        if ((code & 0x4000) != 0) {
            flags.add(TCPFLags.ZERO);
        }
        if ((code & 0x8000) != 0) {
            flags.add(TCPFLags.ZERO);
        }

        return flags;
    }
}
