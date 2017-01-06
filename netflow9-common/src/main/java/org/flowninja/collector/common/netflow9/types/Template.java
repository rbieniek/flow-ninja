/**
 *
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rainer
 *
 */
@AllArgsConstructor
@Getter
public class Template {
	private int flowsetId;
	private List<TemplateField> fields = new LinkedList<>();

	public int getTemplateLength() {
		int length = 0;

		for (TemplateField field : fields) {
			length += field.getLength();
		}

		return length;
	}

}
