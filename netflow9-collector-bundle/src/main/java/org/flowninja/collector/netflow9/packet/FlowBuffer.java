/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.io.Closeable;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class FlowBuffer implements Closeable {
	private int flowSetId;
	private ByteBuf buffer;
	
	public FlowBuffer(int flowSetId, ByteBuf buffer) {
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
}
