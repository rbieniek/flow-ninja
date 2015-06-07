/**
 * 
 */
package org.flowninja.webapp.collector.generic.restcontrollers;

import java.io.UnsupportedEncodingException;

import org.flowninja.security.oauth2.annotations.OAuth2LoggedIn;
import org.flowninja.security.oauth2.types.CollectorClientDetails;
import org.flowninja.types.flows.IPFlow;
import org.flowninja.types.flows.IPFlowCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rainer
 *
 */
@RestController
@OAuth2LoggedIn
public class DataFlowRestContoller {

	@Autowired
	private DataStoreWriter<IPFlow> ipFlowWriter;
	
	@RequestMapping(consumes="application/json", value="/rest/data-flow", method=RequestMethod.PUT)
	public ResponseEntity<Object> storeIPFlow(HttpEntity<IPFlowCollection> requestEntity, 
			@ModelAttribute CollectorClientDetails details) throws UnsupportedEncodingException {

		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
