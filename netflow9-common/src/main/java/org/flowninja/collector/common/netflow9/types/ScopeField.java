/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Scope field transported in an Options template
 *
 * @author rainer
 *
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScopeField {
	private int length;
	private ScopeType type;
}
