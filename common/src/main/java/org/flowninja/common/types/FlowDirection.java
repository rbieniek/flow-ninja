/**
* 
*/
package org.flowninja.common.types;

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
