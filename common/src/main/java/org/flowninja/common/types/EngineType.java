/**
 *
 */
package org.flowninja.common.types;

/**
 * Type of flow switching engine
 *
 * @author rainer
 *
 */
public enum EngineType {
    UNKNOWN,
    ROUTING_PROCESSOR,
    LINE_CARD;

    /**
     * 
     * @param code
     * @return
     */
    public static EngineType fromCode(final int code) {
        switch (code) {
        case 0:
            return ROUTING_PROCESSOR;
        case 1:
            return LINE_CARD;
        default:
            return UNKNOWN;
        }
    }
}
