/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.util.List;

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
public class SetFileNameHeaderTransformer implements Transformer {

	/* (non-Javadoc)
	 * @see org.springframework.integration.transformer.Transformer#transform(org.springframework.messaging.Message)
	 */
	@Override
	public Message<?> transform(Message<?> message) {
		Object payload = message.getPayload();
		
		if(payload instanceof File) {
			message = MessageBuilder.fromMessage(message).setHeader(TransferrerConstants.SOURCE_FILE_HEADER, payload).build();
		} else if(payload instanceof List) {
			if(((List<?>)payload).stream().filter((n) -> !(n instanceof File)).count() > 0) {
				throw new MessagingException("Unsupported payload type");				
			}
						
			message = MessageBuilder.fromMessage(message).setHeader(TransferrerConstants.SOURCE_FILE_LIST_HEADER, payload).build();
		} else {
			throw new MessagingException("Unsupported payload type: " + payload.getClass().getName());
		}
		
		return message;
	}

}
