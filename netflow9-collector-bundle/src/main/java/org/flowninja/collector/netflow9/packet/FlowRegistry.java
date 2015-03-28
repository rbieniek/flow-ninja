/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowninja.collector.common.netflow9.types.Template;

/**
 * @author rainer
 *
 */
public class FlowRegistry {

	private Map<Integer, Template> flowTemplates = new HashMap<Integer, Template>();

	/**
	 * Add a template set to the known templates
	 * 
	 * @param templates
	 */
	public void addFlowTemplates(List<Template> templates) {
		for(Template template : templates) {
			flowTemplates.put(template.getFlowsetId(), template);
		}
	}

	/**
	 * check if a template for a particular flow ID exists
	 * @param flowsetID
	 * @return
	 */
	public boolean hasTemplateForFlowsetID(int flowsetID) {
		return flowTemplates.containsKey(flowsetID);
	}

	public Template templateForFlowsetID(int flowsetID) {
		return flowTemplates.get(flowsetID);
	}
	
}
