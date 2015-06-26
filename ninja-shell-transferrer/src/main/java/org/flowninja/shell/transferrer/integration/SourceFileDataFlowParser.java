/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.types.flows.IPv4Flow;
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
public class SourceFileDataFlowParser {
	private static final Logger logger = LoggerFactory.getLogger(SourceFileDataFlowParser.class);

	@Transformer(inputChannel="sourceDataFileChannel", outputChannel="sourceDataFlowChannel")
	public List<IPv4Flow> parseDataFlows(Message<File> flowFile) {
		List<IPv4Flow> flows = new LinkedList<IPv4Flow>();
		
		return flows;
	}
}
