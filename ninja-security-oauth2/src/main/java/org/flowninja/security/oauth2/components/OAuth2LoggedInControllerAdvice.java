/**
 * 
 */
package org.flowninja.security.oauth2.components;

import org.flowninja.persistence.generic.services.ICollectorPersistenceService;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.security.oauth2.annotations.OAuth2LoggedIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
	
	@Autowired
	private ICollectorPersistenceService collectorService;
	
    /**
     * 
     * @return
     */
	@ModelAttribute
    public OAuth2Authentication authenticationForCurrentRequest() {
            OAuth2Authentication auth = null;
            
            SecurityContext ctx = SecurityContextHolder.getContext();
            
            if(ctx != null && ctx.getAuthentication() != null && (ctx.getAuthentication() instanceof OAuth2Authentication))
                    auth = (OAuth2Authentication)ctx.getAuthentication();

            logger.info("returning current request authentication {}", auth);
            
            return auth;
    }

    @ModelAttribute
	public CollectorRecord bindCallingCollector() {
    	CollectorRecord collector = null;
    	
		OAuth2Authentication auth = authenticationForCurrentRequest();
		
		if(auth != null) {
			collector = collectorService.findCollectoryByClientID(auth.getOAuth2Request().getClientId());
		}
		
		logger.info("resolved calling client to collector {}", collector);
		
		return collector;
	}
	
}
