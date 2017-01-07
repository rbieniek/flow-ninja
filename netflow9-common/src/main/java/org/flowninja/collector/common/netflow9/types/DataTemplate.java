/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class DataTemplate implements NetflowTemplate {
    private final int flowsetId;
    private final List<DataTemplateField> fields;

    public int getTemplateLength() {
        int length = 0;

        for (final DataTemplateField field : fields) {
            length += field.getLength();
        }

        return length;
    }

}
