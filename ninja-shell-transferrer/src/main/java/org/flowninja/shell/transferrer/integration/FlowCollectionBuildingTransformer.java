/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.types.flows.NetworkFlow;
import org.flowninja.types.flows.NetworkFlowCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class FlowCollectionBuildingTransformer {
	private static final Logger logger = LoggerFactory.getLogger(FlowCollectionBuildingTransformer.class);
	
	public Message<?> collectNetworkFlows(Collection<Message<?>> messages) {
		List<File> sourceFiles = new LinkedList<>();
		List<NetworkFlow> flows = new LinkedList<>();
		
		messages.forEach(m -> {
			if(!m.getHeaders().containsKey(TransferrerConstants.SOURCE_FILE_HEADER)) {
				logger.warn("Message {} lacks header {}", m, TransferrerConstants.SOURCE_FILE_HEADER);
				
				throw new MessagingException(m, "Message lacks header " + TransferrerConstants.SOURCE_FILE_HEADER);
			}
			sourceFiles.add(m.getHeaders().get(TransferrerConstants.SOURCE_FILE_HEADER, File.class));

			if(!(m.getPayload() instanceof Collection)) {
				logger.warn("Message has payload of type {} but type {} was expected", 
						m.getPayload().getClass().getName(), Collection.class.getName());
				
				throw new MessagingException(m, "Message has payload of type " + m.getPayload().getClass().getName() 
						+ " but " + NetworkFlow.class.getName() + " was expected");
				
			}
			
			((Collection<?>)m.getPayload()).forEach(f -> {
				if(!(f instanceof NetworkFlow)) {
					logger.warn("Message has payload of type {} but type {} was expected", 
							f.getClass().getName(), NetworkFlow.class.getName());
					
					throw new MessagingException(m, "Message has payload of type " + f.getClass().getName() 
							+ " but " + NetworkFlow.class.getName() + " was expected");
				}
				NetworkFlow flow = (NetworkFlow)f;
				
				flows.add(flow);
			});
		});

		Date firstStamp = null;
		Date lastStamp = null;
		
		for(NetworkFlow f : flows) {
			Date stamp = f.getHeader().getTimestamp();
			
			if(firstStamp == null)
				firstStamp = stamp;
			else if(stamp.before(firstStamp))
				firstStamp = stamp;
			
			if(lastStamp == null)
				lastStamp = stamp;
			else if(stamp.after(lastStamp))
				lastStamp = stamp;
		}
		
		return MessageBuilder.withPayload(new NetworkFlowCollection(firstStamp, lastStamp, flows))
				.setHeader(TransferrerConstants.SOURCE_FILE_LIST_HEADER, sourceFiles)
				.build();
	}
}
