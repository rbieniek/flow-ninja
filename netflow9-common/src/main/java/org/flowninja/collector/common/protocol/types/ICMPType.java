/**
 *
 */
package org.flowninja.collector.common.protocol.types;

/**
 * @author rainer
 *
 */
public enum ICMPType {
    ECHO_REPLY,
    DESTINATION_UNREACHABLE,
    SOURCE_QUENCH,
    REDIRECT,
    ALTERNATE_HOST_ADDRESS,
    ECHO,
    ROUTER_ADVERTISEMENT,
    ROUTER_SELECTION,
    TIME_EXCEEDED,
    PARAMETER_PROBLEM,
    TIMESTAMP,
    TIMESTAMP_REPLY,
    INFORMATION_REQUEST,
    INFORMATION_REPLY,
    ADDRESS_MASK_REQUEST,
    ADDRESS_MASK_REPLY,
    TRACEROUTE,
    DATAGRAM_CONVERSION_ERROR,
    MOBILE_HOST_REDIRECT,
    IPv6_WHERE_ARE_YOU,
    IPv6_I_AM_HERE,
    MOBILE_REGISTRATION_REQUEST,
    MOBILE_REGISTRATION_REPLY,
    DOMAIN_NAME_REQUEST,
    DOMAIN_NAME_REPLY,
    SKIP,
    PHOTURIS,
    UNASSIGNED;

    @SuppressWarnings({ "checkstyle:JavaNCSS", "checkstyle:CyclomaticComplexity" })
    public static ICMPType fromCode(final int code) {
        switch (code) {
        case 0:
            return ECHO_REPLY;
        case 3:
            return DESTINATION_UNREACHABLE;
        case 4:
            return SOURCE_QUENCH;
        case 5:
            return REDIRECT;
        case 6:
            return ALTERNATE_HOST_ADDRESS;
        case 8:
            return ECHO;
        case 9:
            return ROUTER_ADVERTISEMENT;
        case 10:
            return ROUTER_SELECTION;
        case 11:
            return TIME_EXCEEDED;
        case 12:
            return PARAMETER_PROBLEM;
        case 13:
            return TIMESTAMP;
        case 14:
            return TIMESTAMP_REPLY;
        case 15:
            return INFORMATION_REQUEST;
        case 16:
            return INFORMATION_REPLY;
        case 17:
            return ADDRESS_MASK_REQUEST;
        case 18:
            return ADDRESS_MASK_REPLY;
        case 30:
            return TRACEROUTE;
        case 31:
            return DATAGRAM_CONVERSION_ERROR;
        case 32:
            return MOBILE_HOST_REDIRECT;
        case 33:
            return IPv6_WHERE_ARE_YOU;
        case 34:
            return IPv6_I_AM_HERE;
        case 35:
            return MOBILE_REGISTRATION_REQUEST;
        case 36:
            return MOBILE_REGISTRATION_REPLY;
        case 37:
            return DOMAIN_NAME_REQUEST;
        case 38:
            return DOMAIN_NAME_REPLY;
        case 39:
            return SKIP;
        case 40:
            return PHOTURIS;
        default:
            return UNASSIGNED;
        }
    }
}
