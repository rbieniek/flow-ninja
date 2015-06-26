/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.Router;

import java.io.File;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class DataOrOptionsFlowRouter {

	private static final String DATA_CHANNNEL = "sourceDataFileChannel";
	private static final String OPTIONS_CHANNNEL = "sourceOptionsFileChannel";
	private static final String UNPROCESSABLE_CHANNNEL = "unprocessableFileChannel";
	
	@Router(inputChannel="sourceFileChannel", defaultOutputChannel="sourceUnprocessableFileChannel")
	public String routeDataOrOptionsFlow(Message<File> flowFile) {
		String fname = flowFile.getPayload().getName();
		
		if(StringUtils.startsWith(fname, "data-flow-"))
			return DATA_CHANNNEL;
		else if(StringUtils.startsWith(fname, "options-flow-"))
			return OPTIONS_CHANNNEL;
		else
			return UNPROCESSABLE_CHANNNEL;
	}
}
