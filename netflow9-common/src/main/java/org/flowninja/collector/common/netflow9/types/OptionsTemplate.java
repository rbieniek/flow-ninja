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

/**
 * @author rainer
 *
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OptionsTemplate {
	private int flowsetId;
	private List<OptionField> optionFields = new LinkedList<>();
	private List<ScopeField> scopeFields = new LinkedList<>();

	public int getTemplateLength() {
		int length = 0;

		for (ScopeField field : scopeFields) {
			length += field.getLength();
		}

		for (OptionField field : optionFields) {
			length += field.getLength();
		}

		return length;
	}
}
