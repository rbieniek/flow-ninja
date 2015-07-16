/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.flowninja.types.flows.NetworkFlowCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rainer
 *
 */
@Component
public class FlowCollectionStorer implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(FlowCollectionStorer.class);

	@Autowired
	private Environment env;
	
	private File batchTempDir;
	private ObjectMapper mapper;
	
	@SuppressWarnings("deprecation")
	public File storeNetworkFlowCollection(NetworkFlowCollection flowCollection) {
		File collectionFile = null;
		StringBuilder fnameBuilder = new StringBuilder("network-flows-");
		Date stamp = flowCollection.getFirstStamp();

		fnameBuilder.append(String.format("%04d%02d%02d%02d-", stamp.getYear()+1900, stamp.getMonth()+1, stamp.getDate(), stamp.getHours()));
		fnameBuilder.append(UUID.randomUUID().toString());
		fnameBuilder.append(".json");
		
		collectionFile = new File(batchTempDir, fnameBuilder.toString());
		
		logger.info("writing network flows to file {}", collectionFile);
		
		try {
			FileOutputStream fos = new FileOutputStream(collectionFile);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(fos, flowCollection);
			
			try {
				fos.close();
			} catch(IOException ioe) {}
		} catch(IOException e) {
			logger.warn("failed to write JSON network flow collection file at {}", collectionFile, e);
			
			throw new MessagingException("failed to write JSON network flow collection file at " + collectionFile, e);
		}
		
		return collectionFile;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		batchTempDir = new File(new File(env.getRequiredProperty("workDirectory")), "collected-flows");
		
		if(!batchTempDir.isDirectory()) {
			logger.info("creating batch work directory {}", batchTempDir);
			
			if(!batchTempDir.mkdirs()) {
				logger.error("failed to created batch work dir {}", batchTempDir);
				
				throw new IllegalStateException("failed to create batch work directory: " + batchTempDir);
			}
		}
		
		mapper = new ObjectMapper();
	}
	
	
}
