/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author rainer
 *
 */
public class OptionsTemplate {
	private int flowsetId;
	private List<OptionField> optionFields = new LinkedList<OptionField>();
	private List<ScopeField> scopeFields = new LinkedList<ScopeField>();
	
	public OptionsTemplate(int flowsetId, List<ScopeField> scopeFields, List<OptionField> optionFields) {
		this.flowsetId = flowsetId;
		
		if(scopeFields != null)
			this.scopeFields.addAll(scopeFields);
		
		if(optionFields != null)
			this.optionFields.addAll(optionFields);
	}
	
	/**
	 * @return the flowsetId
	 */
	public int getFlowsetId() {
		return flowsetId;
	}
	/**
	 * @return the fields
	 */
	public List<OptionField> getOptionFields() {
		return Collections.unmodifiableList(optionFields);
	}

	/**
	 * @return the scopeFields
	 */
	public List<ScopeField> getScopeFields() {
		return Collections.unmodifiableList(scopeFields);
	}
	
}
