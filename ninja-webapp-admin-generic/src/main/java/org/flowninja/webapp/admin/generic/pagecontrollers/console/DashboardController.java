/**
 * 
 */
package org.flowninja.webapp.admin.generic.pagecontrollers.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Page controller for /console/main page (main console page)
 * 
 * @author rainer
 *
 */
@Controller
public class DashboardController {

	@RequestMapping(value={"/console/dashboard"}, method=RequestMethod.GET)
	public String consoleMain() {
		return "console/dashboard";
	}
}