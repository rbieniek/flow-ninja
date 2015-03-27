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
	
	public void addFlowTemplates(List<Template> templates) {
		for(Template template : templates) {
			flowTemplates.put(template.getFlowsetId(), template);
		}
	}

}
