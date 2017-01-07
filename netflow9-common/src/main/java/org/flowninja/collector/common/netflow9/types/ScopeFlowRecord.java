/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import org.flowninja.collector.common.types.Counter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScopeFlowRecord {
	ScopeType type;
	private Counter value;
}
