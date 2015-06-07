/**
 * 
 */
package org.flowninja.security.oauth2.components;

import org.flowninja.security.oauth2.annotations.OAuth2LoggedIn;
import org.flowninja.security.oauth2.types.CollectorClientDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * This advise handles the evaluation of the {@link LoggedIn} annotation. It evaluates model bindings for {@link AdminUserDetails}
 * 
 * @author rainer
 *
 * @see LoggedIn
 * @see AdminUserDetails
 */
@ControllerAdvice(annotations=OAuth2LoggedIn.class)
public class OAuth2LoggedInControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger(OAuth2LoggedInControllerAdvice.class);
	
	@ModelAttribute
	public CollectorClientDetails bindClientDetails() {
		CollectorClientDetails clientDetails = null;
		
		return clientDetails;
	}
	
}
