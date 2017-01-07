/**
 *
 */
package org.flowninja.collector.netflow9.packet;

import java.io.Closeable;

import org.flowninja.collector.common.netflow9.types.Header;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlowBuffer implements Closeable {
	private Header header;
	private int flowSetId;
	private ByteBuf buffer;

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
