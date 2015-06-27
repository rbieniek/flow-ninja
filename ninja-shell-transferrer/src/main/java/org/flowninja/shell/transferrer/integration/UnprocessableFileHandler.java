/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class UnprocessableFileHandler implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(UnprocessableFileHandler.class);
	
	@Autowired
	private Environment env;
	
	private File deadMessagesDirectory;
	
	@ServiceActivator(inputChannel="sourceUnprocessableFileChannel")
	public void handleUnprocessableFile(Message<File> file) {
		logger.error("Unprocessable file encountered: {}", file.getPayload().getAbsolutePath());
		
		moveFile(file.getPayload());
	}

	@ServiceActivator(inputChannel=IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
	public void handleErrorMessages(Message<File> file) {
		logger.error("Unprocessable file encountered: {}", file.getPayload().getAbsolutePath());
		
		moveFile(file.getPayload());
	}

	@Override
	public void afterPropertiesSet() throws Exception {		
		deadMessagesDirectory = new File(env.getProperty("deadMessagesDirectory"));
		
		if(!deadMessagesDirectory.isDirectory()) {
			if(!deadMessagesDirectory.mkdirs()) {
				throw new IllegalStateException("cannot create dead messages directory: " + deadMessagesDirectory);
			}
		}
	}
	
	private void moveFile(File source) {
		File target = new File(deadMessagesDirectory, source.getName());

		try {
			Files.move(Paths.get(source.toURI()), Paths.get(target.toURI()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("file to move data file {} to {}", source, target, e);
		}
	}
}
