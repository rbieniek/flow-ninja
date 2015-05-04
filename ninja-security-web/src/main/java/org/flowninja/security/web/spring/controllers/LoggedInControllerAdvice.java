/**
 * 
 */
package org.flowninja.security.web.spring.controllers;

import org.flowninja.security.web.spring.annotations.LoggedIn;
import org.flowninja.security.web.spring.types.AdminUserDetails;
import org.flowninja.security.web.spring.types.NotLoggedInException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@ControllerAdvice(annotations=LoggedIn.class)
public class LoggedInControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger(LoggedInControllerAdvice.class);
	
	@ModelAttribute
	public AdminUserDetails bindAdministrator() {
		AdminUserDetails adminDetails = currentPrincipal(AdminUserDetails.class);

		if(adminDetails == null) {
			logger.error("no active user for request");
			
			throw new NotLoggedInException();
		}

		logger.info("Active user key {} for request", adminDetails.getKey());
		
		return adminDetails;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T currentPrincipal(Class<T> clazz) {
		T principal = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null) {
			Object princ = authentication.getPrincipal();
			
			if(clazz.isInstance(princ)) {
				principal = (T)princ;
			}
		}
		
		return principal;
	}
}
