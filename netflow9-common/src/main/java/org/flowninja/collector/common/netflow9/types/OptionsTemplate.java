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
public class OptionsTemplate {
	private int flowsetId;
	private List<OptionField> optionFields = new LinkedList<OptionField>();
	private List<ScopeField> scopeFields = new LinkedList<ScopeField>();
			
	public int getTemplateLength() {
		int length = 0;

		for(ScopeField field : scopeFields)
			length += field.getLength();
		
		for(OptionField field : optionFields)
			length += field.getLength();
		
		return length;
	}
}
