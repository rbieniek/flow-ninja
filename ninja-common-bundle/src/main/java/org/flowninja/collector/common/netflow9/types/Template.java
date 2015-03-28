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
public class Template {
	private int flowsetId;
	private List<TemplateField> fields = new LinkedList<TemplateField>();
	
	public Template(int flowsetId, List<TemplateField> fields) {
		this.flowsetId = flowsetId;
		
		if(fields != null)
			this.fields.addAll(fields);
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
	public List<TemplateField> getFields() {
		return Collections.unmodifiableList(fields);
	}
	
}
