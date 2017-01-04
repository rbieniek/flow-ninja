/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.io.Closeable;

import org.flowninja.collector.common.netflow9.types.Header;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class FlowBuffer implements Closeable {
	private int flowSetId;
	private ByteBuf buffer;
	private Header header;
	
	public FlowBuffer(Header header, int flowSetId, ByteBuf buffer) {
		this.header = header;
		this.flowSetId = flowSetId;
		this.buffer = buffer;
		this.buffer.retain();
	}

	@Override
	public void close() {
		buffer.release();
	}

	/**
	 * @return the flowSetId
	 */
	public int getFlowSetId() {
		return flowSetId;
	}

	/**
	 * @return the buffer
	 */
	public ByteBuf getBuffer() {
		return buffer;
	}

	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}
}
