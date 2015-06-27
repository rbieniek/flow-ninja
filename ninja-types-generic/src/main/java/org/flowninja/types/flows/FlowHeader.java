/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
	
	public static class Builder {
		private FlowHeader header = new FlowHeader();

		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public Builder withRecordCount(int recordCount) {
			header.recordCount = recordCount;
			
			return this;
		}
		
		public Builder withSequenceNumber(int sequenceNumber) {
			header.sequenceNumber = sequenceNumber;
			
			return this;
		}
		
		public Builder withSourceId(long sourceId) {
			header.sourceId = sourceId;
			
			return this;
		}
		
		public Builder withSysUpTime(long sysUpTime) {
			header.sysUpTime = sysUpTime;
			
			return this;
		}
		
		public Builder withTimestamp(Date timestamp) {
			header.timestamp = timestamp;
			header.unixSeconds = timestamp.getTime() / 1000L;
			
			return this;
		}
		
		public Builder withUnixSeconds(long unixSeconds) {
			header.unixSeconds = unixSeconds;
			header.timestamp = new Date(unixSeconds*1000L);
			
			return this;
		}
		
		public FlowHeader build() {
			return header;
		}
	}
	
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FlowHeader))
			return false;
		
		FlowHeader o = (FlowHeader)obj;
		
		return (new EqualsBuilder())
				.append(this.recordCount, o.recordCount)
				.append(this.sequenceNumber, o.sequenceNumber)
				.append(this.sourceId, o.sourceId)
				.append(this.sysUpTime, o.sysUpTime)
				.append(this.timestamp, o.timestamp)
				.append(this.unixSeconds, o.unixSeconds)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(recordCount)
				.append(sequenceNumber)
				.append(sourceId)
				.append(sysUpTime)
				.append(timestamp)
				.append(unixSeconds)
				.toHashCode();
	}
}
