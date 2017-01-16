/**
 *
 */
package org.flowninja.collector.common.protocol.types;

/**
 * IP Protocol version
 * 
 * @author rainer
 *
 */
public enum IPProtocolVersion {
    UNKNOWN,
    IPv4,
    IPv6;

    /**
     * Map from code to value
     * 
     * @param code
     * @return
     */
    public static IPProtocolVersion fromCode(final int code) {
        switch (code) {
        case 4:
            return IPv4;
        case 6:
            return IPv6;
        default:
            return UNKNOWN;
        }
    }
}
