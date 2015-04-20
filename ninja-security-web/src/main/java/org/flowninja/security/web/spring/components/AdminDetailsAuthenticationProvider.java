/**
 * 
 */
package org.flowninja.security.web.spring.components;

import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.security.web.spring.types.AdminUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author rainer
 *
 */
public class AdminDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(AdminDetailsAuthenticationProvider.class);
	
	@Autowired
	private IAdminPersistenceService persistence;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		AdminRecord record = null;
		
		logger.info("Retrieving user details: {}", username);
		
		record = persistence.login(username, (String)authentication.getCredentials());

		if(record == null) {
			logger.warn("Cannot log in user with email address {}", username);
			
			throw new BadCredentialsException("cannot log in user " + username);
		}
		
		
		return new AdminUserDetails(record);
	}

}
