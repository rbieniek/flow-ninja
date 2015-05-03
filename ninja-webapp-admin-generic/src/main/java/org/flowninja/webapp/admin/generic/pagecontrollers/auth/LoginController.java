/**
 * 
 */
package org.flowninja.webapp.admin.generic.pagecontrollers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author rainer
 *
 */
@Controller
public class LoginController {
	
	@RequestMapping(value={"/auth/login"}, method=RequestMethod.GET)
	public String authLogin() {
		return "auth/login";
	}

}
