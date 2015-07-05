/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class DataOrOptionsFlowRouter {

	private static final String DATA_CHANNNEL = "data";
	private static final String OPTIONS_CHANNNEL = "options";
	private static final String UNPROCESSABLE_CHANNNEL = "unprocessable";
	
	public String routeDataOrOptionsFlow(File flowFile) {
		String fname = flowFile.getName();
		
		if(StringUtils.startsWith(fname, "data-flow-"))
			return DATA_CHANNNEL;
		else if(StringUtils.startsWith(fname, "options-flow-"))
			return OPTIONS_CHANNNEL;
		else
			return UNPROCESSABLE_CHANNNEL;
	}
}
