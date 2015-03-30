/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.Template;

/**
 * @author rainer
 *
 */
public class FlowRegistry {

	private Map<Integer, Template> flowTemplates = new HashMap<Integer, Template>();
	private Map<Integer, OptionsTemplate> optionTemplates = new HashMap<Integer, OptionsTemplate>();

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

	public void addOptionTemplates(List<OptionsTemplate> optionsTemplates) {
		for(OptionsTemplate template : optionsTemplates) {
			optionTemplates.put(template.getFlowsetId(), template);
		}
	}
	
	/**
	 * check if a template for a particular flow ID exists
	 * @param flowsetID
	 * @return
	 */
	public boolean hasOptionTemplateForFlowsetID(int flowsetID) {
		return optionTemplates.containsKey(flowsetID);
	}

	public OptionsTemplate optionTemplateForFlowsetID(int flowsetID) {
		return optionTemplates.get(flowsetID);
	}
}
