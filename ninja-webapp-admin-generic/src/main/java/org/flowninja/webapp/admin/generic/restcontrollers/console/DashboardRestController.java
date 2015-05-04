/**
 * 
 */
package org.flowninja.webapp.admin.generic.restcontrollers.console;

import org.flowninja.security.web.spring.annotations.LoggedIn;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rainer
 *
 */
@RestController
@LoggedIn
@RequestMapping(value="/rest/console/dashboard")
public class DashboardRestController {

}
