/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Option field transported in Options template
 *
 * @author rainer
 *
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class OptionField {
	private FieldType type;
	private int length;
}
