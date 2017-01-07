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
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionsFlowRecord {
	FieldType type;
	private Object value;
}
