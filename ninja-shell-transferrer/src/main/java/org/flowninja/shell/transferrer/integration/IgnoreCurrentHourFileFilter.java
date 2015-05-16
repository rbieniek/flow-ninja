/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.stereotype.Component;

/**
 * This filter implementation uses the following conditions for letting a file pass:
 * <ul>
 * <li>The filename must match <code>data-flow-YYYYMMDD-hhmm.json</code> or <code>options-flow-YYYYMMDD-hhmm.json</code></li>
 * <li>The date modeled <code>YYYYMMDD-hh</code> may not match the current time when the filter is called </li>
 *</ul>
 * 
 * @author rainer
 *
 */
@Component
public class IgnoreCurrentHourFileFilter implements FileListFilter<File> {
	private static final Logger logger = LoggerFactory.getLogger(IgnoreCurrentHourFileFilter.class);
	
	@SuppressWarnings("deprecation")
	@Override
	public List<File> filterFiles(File[] files) {
		Date now = new Date();
		String dataFilePrefix = String.format("data-flow-%04d%02d%02d-%02d",
				now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());
		String optionsFilePrefix = String.format("options-flow-%04d%02d%02d-%02d",
				now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());

		List<File> passingFiles = new LinkedList<File>();
		
		for(File file : files) {
			String fname = file.getName();
			
			if(((StringUtils.startsWith(fname, "data-flow-")) || StringUtils.startsWith(fname, "options-flow-")) &&
				StringUtils.endsWith(fname, ".json")) {
				if(!(StringUtils.startsWith(fname, dataFilePrefix) || StringUtils.startsWith(fname, optionsFilePrefix))) {
					logger.info("passing filename {}", fname);
					
					passingFiles.add(file);
				}
					
			}
			
		}
		
		return passingFiles;
	}


}
