/**
 *
 */
package org.flowninja.common.types;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
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

    @JsonIgnore
    public Date getTimestamp() {
        return new Date(unixSeconds * 1000L);
    }
}
