/**
 *
 */
package org.flowninja.collector.common.protocol.types;

/**
 * @author rainer
 *
 */
public enum ForwardingStatus {
    UNKNOWN,
    FORWARD_UNKNOWN,
    FORWARD_FRAGMENTED,
    FORWARD_NOT_FRAGMENTED,
    DROP_UNKNOWN,
    DROP_ACL_DENY,
    DROPD_ACL_DROP,
    DROP_UNROUTABLE,
    DROP_ADJACENCY,
    DROP_FRAGMENTATION_AND_DF_SET,
    DROP_BAD_HEADER_CHECKSUM,
    DROP_BAD_TOTAL_LENGTH,
    DROP_BAD_HEADER_LENGTH,
    DROP_BAD_TTL,
    DROP_POLICER,
    DROP_WRED,
    DROP_RPF,
    DROP_FOR_US,
    DROP_BAD_OUTPUT_INTERFACE,
    DROP_HARDWARE,
    CONSUME_UNKNONW,
    TERMINATE_PUNT_ADJACENCY,
    TERMINATE_INCOMPLETE_ADJACENCY,
    TERMINATE_FOR_US;

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public static ForwardingStatus fromCode(final int code) {
        switch (code) {
        case 64:
            return FORWARD_UNKNOWN;
        case 65:
            return FORWARD_FRAGMENTED;
        case 66:
            return FORWARD_NOT_FRAGMENTED;
        case 128:
            return DROP_UNKNOWN;
        case 129:
            return DROP_ACL_DENY;
        case 130:
            return DROPD_ACL_DROP;
        case 131:
            return DROP_UNROUTABLE;
        case 132:
            return DROP_ADJACENCY;
        case 133:
            return DROP_FRAGMENTATION_AND_DF_SET;
        case 134:
            return DROP_BAD_HEADER_CHECKSUM;
        case 135:
            return DROP_BAD_TOTAL_LENGTH;
        case 136:
            return DROP_BAD_HEADER_LENGTH;
        case 137:
            return DROP_BAD_TTL;
        case 138:
            return DROP_POLICER;
        case 139:
            return DROP_WRED;
        case 140:
            return DROP_RPF;
        case 141:
            return DROP_FOR_US;
        case 142:
            return DROP_BAD_OUTPUT_INTERFACE;
        case 143:
            return DROP_HARDWARE;
        case 192:
            return CONSUME_UNKNONW;
        case 193:
            return TERMINATE_PUNT_ADJACENCY;
        case 194:
            return TERMINATE_INCOMPLETE_ADJACENCY;
        case 195:
            return TERMINATE_FOR_US;

        default:
            return UNKNOWN;
        }
    }
}
