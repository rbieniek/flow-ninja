/**
 * 
 */
package org.flowninja.webapp.admin.generic.pagecontrollers.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author rainer
 *
 */
public class LoginController {
	
	@RequestMapping(value={"/auth/login"}, method=RequestMethod.GET)
	public String index() {
		return "auth/login";
	}

}
