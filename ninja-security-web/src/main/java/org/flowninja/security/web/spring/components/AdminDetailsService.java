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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implements the user details service for administrative users
 * 
 * @author rainer
 *
 */
public class AdminDetailsService implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(AdminDetailsService.class);
	
	@Autowired
	private IAdminPersistenceService persistence;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("load user by name: {}", username);
		
		AdminRecord record = persistence.findByUserName(username);

		if(record == null) {
			logger.info("User name not found: {}", username);
			
			throw new UsernameNotFoundException(username);
		}
		
		return new AdminUserDetails(record);
	}

}
