/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rainer
 *
 */
public enum IPv6OptionHeaders {
    RESERVED,
    FRA_X,
    RH,
    FRA_0,
    UNK,
    HOP,
    DST,
    PAY,
    AH,
    ESP;

    public static Set<IPv6OptionHeaders> fromCode(final int code) {
        final Set<IPv6OptionHeaders> headers = new HashSet<>();

        for (int i = 0; i < 32; i++) {
            if ((code & 1 << i) != 0) {
                switch (i) {
                case 1:
                    headers.add(FRA_X);
                    break;
                case 2:
                    headers.add(RH);
                    break;
                case 3:
                    headers.add(FRA_0);
                    break;
                case 4:
                    headers.add(UNK);
                    break;
                case 6:
                    headers.add(HOP);
                    break;
                case 7:
                    headers.add(DST);
                    break;
                case 8:
                    headers.add(PAY);
                    break;
                case 9:
                    headers.add(AH);
                    break;
                case 10:
                    headers.add(ESP);
                    break;
                default:
                    headers.add(RESERVED);
                    break;
                }
            }
        }

        return headers;
    }
}
