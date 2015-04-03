/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class TemplateOutputFile {
	private static final Logger logger = LoggerFactory.getLogger(TemplateOutputFile.class);
	
	private File baseDirectory;
	private String template;
	private String writeFileName;
	private PrintWriter writer;
	
	public TemplateOutputFile(File baseDirectory, String template) {
		this.baseDirectory = baseDirectory;
		this.template = template;
	}
	
	public void close() {
		if(writer != null) {
			internalClose();
		}
	}
	
	public PrintWriter getWriter() throws IOException {
		try {
			if(writer != null) {
				String tmpName = buildWriteFileName();
				
				if(!StringUtils.equals(writeFileName, tmpName)) {
					internalClose();
					
					writeFileName = tmpName;
					writer = new PrintWriter(new File(baseDirectory, writeFileName));
				}
			} else {
				writeFileName = buildWriteFileName();
				writer = new PrintWriter(new File(baseDirectory, writeFileName));
			}
			
			return writer;
		} catch(IOException e) {
			logger.warn("failed to open output file {}", writeFileName, e);
			
			throw e;
		}
	}

	@SuppressWarnings("deprecation")
	private String buildWriteFileName() {
		Map<String, Object> values = new HashMap<String, Object>();
		StrSubstitutor sub = new StrSubstitutor(values);
		Date date = new Date();
		int year = date.getYear();
		
		values.put("yyyy", String.format("%04.4d",year + 1900));
		values.put("yy", String.format("%02.2d", year > 100 ? year - 100 : year));
		values.put("mm", String.format("%02.2d", date.getMonth()+1));
		values.put("dd", String.format("%02.2d", date.getDate()));
		values.put("HH", String.format("%02.2d", date.getHours()));
		values.put("MM", String.format("%02.2d", date.getMinutes()));
		values.put("SS", String.format("%02.2d", date.getSeconds()));

		return sub.replace(template);
	}
	
	private void internalClose() {
		try {
			writer.close();
		} finally {
			writer = null;
			writeFileName = null;
		}
	}
}
