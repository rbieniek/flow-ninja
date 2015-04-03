/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	private OutputStream stream;
	
	public TemplateOutputFile(File baseDirectory, String template) {
		this.baseDirectory = baseDirectory;
		this.template = template;
	}
	
	public void close() {
		if(stream != null) {
			internalClose();
		}
	}
	
	public PrintWriter getWriter() throws IOException {
		try {
			if(stream != null) {
				logger.info("detected open stream to file name {}", writeFileName);
				
				String tmpName = buildWriteFileName();
				
				if(!StringUtils.equals(writeFileName, tmpName)) {
					logger.info("detected changed file name from {} to {}", writeFileName, tmpName);
					
					internalClose();
					
					writeFileName = tmpName;
					stream = new FileOutputStream(new File(baseDirectory, writeFileName));
				}
			} else {
				logger.info("opening print writer to file name {}", writeFileName);

				writeFileName = buildWriteFileName();
				stream = new FileOutputStream(new File(baseDirectory, writeFileName));
			}
			
			return new PrintWriter(new NonCloseableProxyOutputStream(stream), true);
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
		
		values.put("yyyy", String.format("%04d",year + 1900));
		values.put("yy", String.format("%02d", year > 100 ? year - 100 : year));
		values.put("mm", String.format("%02d", date.getMonth()+1));
		values.put("dd", String.format("%02d", date.getDate()));
		values.put("HH", String.format("%02d", date.getHours()));
		values.put("MM", String.format("%02d", date.getMinutes()));
		values.put("SS", String.format("%02d", date.getSeconds()));

		String fileName = sub.replace(template);
		
		logger.info("build write file name: {}", fileName);
		
		return fileName;
	}
	
	private void internalClose() {
		try {
			stream.close();
		} catch(IOException e) {
			logger.warn("failed to close stream {}", writeFileName, e);
		} finally {
			stream = null;
			writeFileName = null;
		}
	}
}
