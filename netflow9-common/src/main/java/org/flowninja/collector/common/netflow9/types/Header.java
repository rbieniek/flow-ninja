/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import java.io.Serializable;
import java.util.Date;

/**
 * Packet header leading in a Netflow 9 packet
 * 
 * @author rainer
 *
 */
public class Header implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6322350109524223750L;
	
	private int recordCount;
	private long sysUpTime;
	private long unixSeconds;
	private long sequenceNumber;
	private long sourceId;
	private Date timestamp;
	
	public Header() {}
	
	public Header(int recordCount, long sysUpTime, long unixSeconds, long sequenceNumber, long sourceId) {
		this.recordCount = recordCount;
		this.sysUpTime = sysUpTime;
		this.unixSeconds = unixSeconds;
		this.sequenceNumber = sequenceNumber;
		this.sourceId = sourceId;
		this.timestamp = new Date(unixSeconds * 1000L);
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * @return the sysUpTime
	 */
	public long getSysUpTime() {
		return sysUpTime;
	}

	/**
	 * @return the unixSeconds
	 */
	public long getUnixSeconds() {
		return unixSeconds;
	}

	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @return the sourceId
	 */
	public long getSourceId() {
		return sourceId;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
