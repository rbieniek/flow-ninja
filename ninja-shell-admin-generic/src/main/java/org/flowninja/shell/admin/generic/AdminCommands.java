/**
 * 
 */
package org.flowninja.shell.admin.generic;

import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;

/**
 * @author rainer
 *
 */
public class AdminCommands implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(AdminCommands.class);
	
	@Autowired
	private IAdminPersistenceService service;
}
