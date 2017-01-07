/**
 *
 */
package org.flowninja.collector.common.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EncodedData {
	private byte[] data;
	private String encodedData;
}
