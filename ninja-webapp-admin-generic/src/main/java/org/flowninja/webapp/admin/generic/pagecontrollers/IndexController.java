/**
 * 
 */
package org.flowninja.webapp.admin.generic.pagecontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Page controller for /index page (main webapp page)
 * 
 * @author rainer
 *
 */
@Controller
public class IndexController {

	@RequestMapping(value={"/", "/index"}, method=RequestMethod.GET)
	public String index() {
		return "index";
	}
}