/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Packet header leading in a Netflow 9 packet
 *
 * @author rainer
 *
 */
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class Header implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6322350109524223750L;

    // do not change field order
    private int recordCount;
    private long sysUpTime;
    private long unixSeconds;
    private long sequenceNumber;
    private long sourceId;

    public Date getTimestamp() {
        return new Date(unixSeconds*1000L);
    }
}
