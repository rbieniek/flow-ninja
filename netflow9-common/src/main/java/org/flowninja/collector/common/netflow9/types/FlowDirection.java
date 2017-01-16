/**
* 
*/
package org.flowninja.collector.common.netflow9.types;

/**
 * @author rainer
 *
 */
public enum FlowDirection {
    UNKNOWN,
    INGRESS,
    EGRESS;

    public static final FlowDirection fromCode(final int code) {
        switch (code) {
        case 0:
            return INGRESS;
        case 1:
            return EGRESS;
        default:
            return UNKNOWN;
        }
    }
}
