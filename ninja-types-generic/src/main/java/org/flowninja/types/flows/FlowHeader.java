/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Packet header leading in a Netflow 9 packet
 * 
 * @author rainer
 *
 */
public class FlowHeader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6322350109524223750L;
	
	@JsonProperty(value="recordCount", required=true)
	private int recordCount;

	@JsonProperty(value="sysUpTime", required=true)
	private long sysUpTime;
	
	@JsonProperty(value="unixSeconds", required=true)
	private long unixSeconds;

	@JsonProperty(value="sequenceNumber", required=true)
	private long sequenceNumber;

	@JsonProperty(value="sourceId", required=true)
	private long sourceId;

	@JsonProperty(value="stamp", required=true)
	@JsonFormat(shape=Shape.STRING)
	private Date timestamp;
	
	public FlowHeader() {}
	
	public FlowHeader(int recordCount, long sysUpTime, long unixSeconds, long sequenceNumber, long sourceId) {
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
