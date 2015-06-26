/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class UnprocessableFileHandler {
	private static final Logger logger = LoggerFactory.getLogger(UnprocessableFileHandler.class);
	
	@ServiceActivator(inputChannel="sourceUnprocessableFileChannel")
	public void handleUnprocessableFile(Message<File> file) {
		logger.error("Unprocessable file encountered: {}", file.getPayload().getAbsolutePath());
	}
}
