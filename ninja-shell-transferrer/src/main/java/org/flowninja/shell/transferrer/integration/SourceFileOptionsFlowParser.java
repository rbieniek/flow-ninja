/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.types.flows.IPv4Flow;
import org.flowninja.types.flows.OptionsFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class SourceFileOptionsFlowParser {
	private static final Logger logger = LoggerFactory.getLogger(SourceFileOptionsFlowParser.class);

	@Transformer(inputChannel="sourceOptionsFileChannel", outputChannel="sourceOptionsFlowChannel")
	public List<OptionsFlow> parseDataFlows(Message<File> flowFile) {
		List<OptionsFlow> flows = new LinkedList<OptionsFlow>();
		
		return flows;
	}
}
