/**
 *
 */
package org.flowninja.collector.common.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Container class for returning a enum alongside with it's raw code value
 * 
 * @author rainer
 *
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class EnumCodeValue<T extends Enum<?>> {
	private final T value;
	private final int code;
}
