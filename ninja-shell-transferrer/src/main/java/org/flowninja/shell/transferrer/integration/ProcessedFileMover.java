/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class ProcessedFileMover implements MessageHandler, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ProcessedFileMover.class);

	private static final String DATA_PREFIX = "data-flow-";
	private static final String OPTIONS_PREFIX = "options-flow-";
	private static final int DATA_PREFIX_LEN;
	private static final int OPTIONS_PREIX_LEN;
	
	static {
		DATA_PREFIX_LEN = DATA_PREFIX.length();
		OPTIONS_PREIX_LEN = OPTIONS_PREFIX.length();
	}
	
	@Autowired
	private Environment env;
	
	private File atticDirectory;
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if(message.getHeaders().containsKey(TransferrerConstants.SOURCE_FILE_LIST_HEADER)) {
			@SuppressWarnings("unchecked")
			List<File> sourceFiles = message.getHeaders().get(TransferrerConstants.SOURCE_FILE_LIST_HEADER, List.class);
	
			for(File sourceFile : sourceFiles) {
				String fname = sourceFile.getName();
				String datePart = null;
				
				if(StringUtils.startsWith(fname, DATA_PREFIX)) {
					datePart = fname.substring(DATA_PREFIX_LEN);
				} else if(StringUtils.startsWith(fname, OPTIONS_PREFIX)) {
					datePart = fname.substring(OPTIONS_PREIX_LEN);
				}
				
				if(StringUtils.length(datePart) > 8) {
					File year = new File(atticDirectory, StringUtils.substring(datePart, 0, 4));
					File month = new File(year, StringUtils.substring(datePart, 4, 6));
					File day = new File(month, StringUtils.substring(datePart, 6, 8));
					
					day.mkdirs();
					
					File target = new File(day, sourceFile.getName());
					
					try {
						Files.move(Paths.get(sourceFile.toURI()), Paths.get(target.toURI()), 
								StandardCopyOption.REPLACE_EXISTING);						
					} catch (IOException e) {
						logger.error("file to move data file {} to {}", sourceFile, target, e);
					}
					
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.atticDirectory = new File(env.getRequiredProperty("processedDirectory"));
		
		if(!this.atticDirectory.isDirectory()) {
			logger.info("creating attic directoty {}", this.atticDirectory);
			
			if(!this.atticDirectory.mkdirs()) {
				logger.info("cannot create attic directoty {}", this.atticDirectory);
				
				throw new IllegalStateException("cannot create attic directory " + this.atticDirectory);
			}
		}
	}


}
