/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.NetflowTemplate;

/**
 * @author rainer
 *
 */
@Component
@Scope("prototype")
public class TemplateRegistry<T extends NetflowTemplate> {

    private Map<Integer, T> flowTemplates = new HashMap<>();

    /**
     * Add a template set to the known templates
     *
     * @param templates
     */
    public void addTemplates(final List<T> templates) {
        for (T template : templates) {
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

    public T templateForFlowsetID(final int flowsetID) {
        return flowTemplates.get(flowsetID);
    }

}
