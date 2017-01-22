/**
 *
 */
package org.flowninja.common.types;

/**
 * Sampling algorithm used in collector
 *
 * @author rainer
 *
 */
public enum SamplingAlgorithm {
    UNKNOWN,
    DETERMINISTIC,
    RANDOM;

    /**
     * map from integer code to value
     * 
     * @param code
     * @return
     */
    public static SamplingAlgorithm fromCode(final int code) {
        switch (code) {
        case 1:
            return DETERMINISTIC;
        case 2:
            return RANDOM;
        default:
            return UNKNOWN;
        }
    }
}
