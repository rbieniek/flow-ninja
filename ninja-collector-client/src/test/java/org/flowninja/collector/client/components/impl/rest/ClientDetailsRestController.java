/**
 * 
 */
package org.flowninja.collector.client.components.impl.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rainer
 *
 */
@RestController
public class ClientDetailsRestController {
	private static final Logger logger = LoggerFactory.getLogger(ClientDetailsRestController.class);
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE, value="/rest/client-details")
	public ResponseEntity<RestClientDetails> clientDetails() {
		try {
			OAuth2AuthenticationDetails authDetails = (OAuth2AuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			
			return new ResponseEntity<RestClientDetails>(new RestClientDetails(authDetails.getTokenValue(), authDetails.getTokenType()), 
					HttpStatus.OK);
		} catch(Exception e) {
			logger.error("failed to retrieve client details", e);
			
			return new ResponseEntity<RestClientDetails>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
