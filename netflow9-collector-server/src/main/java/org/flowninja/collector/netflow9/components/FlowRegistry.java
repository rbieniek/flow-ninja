/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.Template;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class FlowRegistry {

	private Map<Integer, Template> flowTemplates = new HashMap<>();
	private Map<Integer, OptionsTemplate> optionTemplates = new HashMap<>();
	private List<FlowBuffer> backlogFlows = new LinkedList<>();

	/**
	 * Add a template set to the known templates
	 *
	 * @param templates
	 */
	public void addFlowTemplates(final List<Template> templates) {
		for (Template template : templates) {
			flowTemplates.put(template.getFlowsetId(), template);
		}
	}

	/**
	 * check if a template for a particular flow ID exists
	 *
	 * @param flowsetID
	 * @return
	 */
	public boolean hasTemplateForFlowsetID(final int flowsetID) {
		return flowTemplates.containsKey(flowsetID);
	}

	public Template templateForFlowsetID(final int flowsetID) {
		return flowTemplates.get(flowsetID);
	}

	public void addOptionTemplates(final List<OptionsTemplate> optionsTemplates) {
		for (OptionsTemplate template : optionsTemplates) {
			optionTemplates.put(template.getFlowsetId(), template);
		}
	}

	/**
	 * check if a template for a particular flow ID exists
	 *
	 * @param flowsetID
	 * @return
	 */
	public boolean hasOptionTemplateForFlowsetID(final int flowsetID) {
		return optionTemplates.containsKey(flowsetID);
	}

	public OptionsTemplate optionTemplateForFlowsetID(final int flowsetID) {
		return optionTemplates.get(flowsetID);
	}

	/**
	 * Retrieve the list of backlogs flows for which a matching template record
	 * has not been received
	 *
	 * @return
	 */
	public Collection<? extends FlowBuffer> backlogFlows() {
		return backlogFlows;
	}

	/**
	 * Set the list of backlog flows
	 *
	 * @param flows
	 */
	public void backlogFlows(final List<FlowBuffer> flows) {
		this.backlogFlows.clear();

		if (flows != null) {
			this.backlogFlows.addAll(flows);
		}
	}
}
