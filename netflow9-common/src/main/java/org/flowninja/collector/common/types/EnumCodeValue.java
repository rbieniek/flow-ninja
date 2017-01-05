/**
 *
 */
package org.flowninja.collector.common.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Container class for returning a enum alongside with it's raw code value
 * @author rainer
 *
 */
@Getter
@ToString
@EqualsAndHashCode
public class EnumCodeValue<T extends Enum<?>> {
    private final T value;
    private final int code;

    public EnumCodeValue(T  value, int code) {
        this.value = value;
        this.code = code;
    }

}
