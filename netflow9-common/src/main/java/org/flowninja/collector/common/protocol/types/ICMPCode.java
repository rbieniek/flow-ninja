/**
 *
 */
package org.flowninja.collector.common.protocol.types;

/**
 * @author rainer
 *
 */
public enum ICMPCode {
    UNASSIGNED,
    NO_CODE,

    // Type 3 - Destination unreachable
    NET_UNREACHABLE,
    HOST_UNREACABLE,
    PROTOCOL_UNREACHABLE,
    PORT_UNREACHABLE,
    FRAGMENTATION_NEEDED,
    SOURCE_ROUTE_FAILED,
    DESTINATION_NETWORK_UNKNWON,
    DESTINATION_HOST_UNKNOWN,
    SOURCE_HOST_ISOLATED,
    COMMUNICATION_DESTINATION_NETWORK_ADMINSTRATIVELY_PROHIBITED,
    COMMUNICATION_DESTINATION_HOST_ADMINSTRATIVELY_PROHIBITED,
    DESTINATION_NETWORK_UNREACHABLE,
    DESTINATION_HOST_UNREACHABLE,
    COMMUNICATION_ADMINSTRATIVELY_PROHIBITED,
    HOST_PRECEDENCE_VIOLATION,
    PRECEDENCE_CUTOFF,

    // Type 5 - Redirect
    REDIRECT_FOR_NETWORK,
    REDIRECT_FOR_HOST,
    REDIRECT_FOR_TOS_AND_NETWORK,
    REDIRECT_FOR_TOS_AND_HOST,

    // Type 6 - Alternate Host Address
    ALTERNATE_ADDRESS_FOR_HOST,

    // Type 9 - Router advertisement
    NORMAL_ROUTER_ADVERTISEMENT,
    DOES_NOT_ROUTE_COMMON_TRAFFIC,

    // Type 11 - Time exceeded
    TTL_EXCEED_IN_TRANSIT,
    REASSEMABLY_TIME_EXCEEDED,

    // Type 12 - Parameter problem
    POINTER_INDICATES_ERROR,
    MISSING_REQUIRED_OPTION,
    BAD_LENGTH,

    // Type 40 - Photuris
    BAD_SPI,
    AUTHENTICATION_FAILED,
    DECOMPRESSION_FAILED,
    DECRYPTION_FAILED,
    NEED_AUTHENTICATION,
    NEED_AUTHORIZATION;

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public static ICMPCode fromCodeForType3(final int code) {
        switch (code) {
        case 0:
            return NET_UNREACHABLE;
        case 1:
            return HOST_UNREACABLE;
        case 2:
            return PROTOCOL_UNREACHABLE;
        case 3:
            return PORT_UNREACHABLE;
        case 4:
            return FRAGMENTATION_NEEDED;
        case 5:
            return SOURCE_ROUTE_FAILED;
        case 6:
            return DESTINATION_NETWORK_UNKNWON;
        case 7:
            return DESTINATION_HOST_UNKNOWN;
        case 8:
            return SOURCE_HOST_ISOLATED;
        case 9:
            return COMMUNICATION_DESTINATION_NETWORK_ADMINSTRATIVELY_PROHIBITED;
        case 10:
            return COMMUNICATION_DESTINATION_HOST_ADMINSTRATIVELY_PROHIBITED;
        case 11:
            return DESTINATION_NETWORK_UNREACHABLE;
        case 12:
            return DESTINATION_HOST_UNREACHABLE;
        case 13:
            return COMMUNICATION_ADMINSTRATIVELY_PROHIBITED;
        case 14:
            return HOST_PRECEDENCE_VIOLATION;
        case 15:
            return PRECEDENCE_CUTOFF;
        default:
            return UNASSIGNED;
        }
    }

    public static ICMPCode fromCodeForType5(final int code) {
        switch (code) {
        case 0:
            return REDIRECT_FOR_NETWORK;
        case 1:
            return REDIRECT_FOR_HOST;
        case 2:
            return REDIRECT_FOR_TOS_AND_NETWORK;
        case 3:
            return REDIRECT_FOR_TOS_AND_HOST;
        default:
            return UNASSIGNED;
        }
    }

    @SuppressWarnings("PMD")
    public static ICMPCode fromCodeForType6(final int code) {
        switch (code) {
        case 0:
            return ALTERNATE_ADDRESS_FOR_HOST;
        default:
            return UNASSIGNED;
        }
    }

    @SuppressWarnings("PMD")
    public static ICMPCode fromCodeForType9(final int code) {
        switch (code) {
        case 0:
            return NORMAL_ROUTER_ADVERTISEMENT;
        case 16:
            return DOES_NOT_ROUTE_COMMON_TRAFFIC;
        default:
            return UNASSIGNED;
        }
    }

    @SuppressWarnings("PMD")
    public static ICMPCode fromCodeForType11(final int code) {
        switch (code) {
        case 0:
            return TTL_EXCEED_IN_TRANSIT;
        case 1:
            return REASSEMABLY_TIME_EXCEEDED;
        default:
            return UNASSIGNED;
        }
    }

    public static ICMPCode fromCodeForType12(final int code) {
        switch (code) {
        case 0:
            return POINTER_INDICATES_ERROR;
        case 1:
            return MISSING_REQUIRED_OPTION;
        case 2:
            return BAD_LENGTH;
        default:
            return UNASSIGNED;
        }
    }

    public static ICMPCode fromCodeForType40(final int code) {
        switch (code) {
        case 0:
            return BAD_SPI;
        case 1:
            return AUTHENTICATION_FAILED;
        case 2:
            return DECOMPRESSION_FAILED;
        case 3:
            return DECRYPTION_FAILED;
        case 4:
            return NEED_AUTHENTICATION;
        case 5:
            return NEED_AUTHORIZATION;
        default:
            return UNASSIGNED;
        }
    }
}
