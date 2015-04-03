/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles all file system related stuff for writing flat netflow 9 records
 * 
 * @author rainer
 *
 */
public class FileSystemHandler {
	private static final Logger logger = LoggerFactory.getLogger(FileSystemHandler.class);
	
	private String baseDirectoryPath;
	private String dataFileNameTemplate;
	private String optionsFileNameTemplate;
	private File baseDirectory;
	private TemplateOutputFile dataFile;
	private TemplateOutputFile optionsFile;
	
	/**
	 * @param baseDirectory the baseDirectory to set
	 */
	public void setBaseDirectoryPath(String baseDirectory) {
		this.baseDirectoryPath = baseDirectory;
	}

	/**
	 * @param fileNameTemplate the fileNameTemplate to set
	 */
	public void setDataFileNameTemplate(String fileNameTemplate) {
		this.dataFileNameTemplate = fileNameTemplate;
	}

	/**
	 * Init method called whenever the bundle starts or restarts
	 */
	public void init() {
		logger.info("starting file system handler for Netflow9 flat-file store");

		baseDirectory = new File(this.baseDirectoryPath);
		
		if(!baseDirectory.exists()) {
			logger.info("creating base directory ‘{}‘", baseDirectoryPath);
			
			if(!baseDirectory.mkdirs()) {
				throw new IllegalStateException("Failed to " + baseDirectoryPath);
			}
		}
		
		dataFile = new TemplateOutputFile(baseDirectory, dataFileNameTemplate);
		optionsFile = new TemplateOutputFile(baseDirectory, optionsFileNameTemplate);
	}

	public PrintWriter getDataFlowWriter() throws IOException {
		return dataFile.getWriter();
	}
	
	public PrintWriter getOptionsFlowWriter() throws IOException {
		return optionsFile.getWriter();
	}
	
	/**
	 * Destroy method
	 */
	public void destroy() {
		logger.info("stopping file system handler for Netflow9 flat-file store");
		
		dataFile.close();
		optionsFile.close();
	}

	/**
	 * @param optionsFileNameTemplate the optionsFileNameTemplate to set
	 */
	public void setOptionsFileNameTemplate(String optionsFileNameTemplate) {
		this.optionsFileNameTemplate = optionsFileNameTemplate;
	}

}
