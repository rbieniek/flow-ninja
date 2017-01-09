/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.LinkedList;
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
public class OptionsTemplate implements NetflowTemplate {
    private final int flowsetId;
    private List<OptionField> optionFields = new LinkedList<>();
    private List<ScopeField> scopeFields = new LinkedList<>();

    public int getTemplateLength() {
        int length = 0;

        for (final ScopeField field : scopeFields) {
            length += field.getLength();
        }

        for (final OptionField field : optionFields) {
            length += field.getLength();
        }

        return length;
    }
}