/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import org.flowninja.collector.common.types.Counter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class ScopeFlowRecord {

    private ScopeType type;
    private Counter value;
}
