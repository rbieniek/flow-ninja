/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

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
public class Template {
	private int flowsetId;
	private List<TemplateField> fields;

	public int getTemplateLength() {
		int length = 0;

		for (TemplateField field : fields) {
			length += field.getLength();
		}

		return length;
	}

}
