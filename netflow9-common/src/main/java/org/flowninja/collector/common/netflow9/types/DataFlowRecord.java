/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

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
public class DataFlowRecord {
	private FieldType type;
	private Object value;

}
