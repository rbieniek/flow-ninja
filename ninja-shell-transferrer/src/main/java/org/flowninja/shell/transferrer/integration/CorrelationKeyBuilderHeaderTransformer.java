/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class CorrelationKeyBuilderHeaderTransformer implements Transformer {
	private static final Logger logger = LoggerFactory.getLogger(CorrelationKeyBuilderHeaderTransformer.class);

	private static final String DATA_FLOW_PREFIX = "data-flow-";
	private static final int DATA_FLOW_PREFIX_LEN = 10;
	private static final String OPTIONS_FLOW_PREFIX = "options-flow-";
	private static final int OPTIONS_FLOW_PREFIX_LEN = 13;
	private static final String JSON_SUFFIX = ".json";
	private static final int STAMP_LEN = 11;
	
	@Override
	public Message<?> transform(Message<?> message) {
		Object payload = message.getPayload();
		
		if(payload instanceof File) {
			String fname = ((File)payload).getName();
			
			if(StringUtils.startsWith(fname, DATA_FLOW_PREFIX) && StringUtils.endsWith(fname, JSON_SUFFIX)) {
				fname = StringUtils.substring(fname, 0, DATA_FLOW_PREFIX_LEN + STAMP_LEN);
				fname = StringUtils.remove(fname, '-');
				message = MessageBuilder.fromMessage(message).setHeader(TransferrerConstants.CORRELATION_HEADER, fname).build();
				
			} else if(StringUtils.startsWith(fname, OPTIONS_FLOW_PREFIX) && StringUtils.endsWith(fname, JSON_SUFFIX)) {
				fname = StringUtils.substring(fname, 0, OPTIONS_FLOW_PREFIX_LEN+STAMP_LEN);
				fname = StringUtils.remove(fname, '-');
				
				message = MessageBuilder.fromMessage(message).setHeader(TransferrerConstants.CORRELATION_HEADER, fname).build();
			}
		} else {
			throw new MessagingException("Unsupported payload type: " + payload.getClass().getName());
		}
		
		logger.info("transformed message {}", message);

		return message;
	}

	
}
