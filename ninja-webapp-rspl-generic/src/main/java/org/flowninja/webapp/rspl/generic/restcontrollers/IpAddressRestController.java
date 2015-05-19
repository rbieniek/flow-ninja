/**
 * 
 */
package org.flowninja.webapp.rspl.generic.restcontrollers;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService;
import org.flowninja.persistence.rspl.generic.services.RsplPersistenceService;
import org.flowninja.rspl.client.json.RSPLServiceJsonClient;
import org.flowninja.rspl.client.whois.RSPLServiceWhoisClient;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rainer
 *
 */
@RestController
public class IpAddressRestController {
	private static final Logger logger = LoggerFactory.getLogger(IpAddressRestController.class);
		
	@Autowired
	private IBlockedNetworksPersistenceService blockednetworkService;
	
	@Autowired
	private RsplPersistenceService rsplService;
	
	@Autowired
	private RSPLServiceJsonClient jsonClient;
	
	@Autowired
	private RSPLServiceWhoisClient whoisClient;
	
	@RequestMapping(produces="application/json", method=RequestMethod.GET, value="/rest/ip/address")
	public ResponseEntity<NetworkResource> ipAddr(@RequestParam(value="ipv4", required=false) String ipAddr) {
		CIDR4Address cidr;
		
		if(StringUtils.isBlank(ipAddr)) {
			logger.info("No IPv4 address given");
			
			return new ResponseEntity<NetworkResource>(HttpStatus.BAD_REQUEST);
		}
		
		logger.info("looking up IP address: '{}'", ipAddr);
		
		try {
			cidr = new CIDR4Address(NetworkAddressHelper.parseAddressSpecification(ipAddr));
		} catch(IllegalArgumentException e) {
			logger.info("failed to parse passed address specification", e);
			
			return new ResponseEntity<NetworkResource>(HttpStatus.BAD_REQUEST);
		}
		
		if(blockednetworkService.findContainingBlockedNetwork(cidr) != null) {
			logger.info("IP address {} is in adminstratively blocked network range", cidr);
			
			return new ResponseEntity<NetworkResource>(HttpStatus.NOT_FOUND);
		}

		NetworkResource resource = rsplService.loadNetworkResource(cidr);
		
		if(resource != null) {
			logger.info("looked of IP Address {} yielded cache lookup result {}", cidr, resource);
			
			return new ResponseEntity<NetworkResource>(resource, HttpStatus.OK);
		}

		if(jsonClient.canResolveAddress(cidr)) {
			resource = jsonClient.resolveAddress(cidr);
			
			if(resource != null) {
				rsplService.persistNetworkResource(resource);

				logger.info("looked of IP Address {} yielded JSON WHOIS lookup result {}", cidr, resource);

				return new ResponseEntity<NetworkResource>(resource, HttpStatus.OK);
			}
		} else if(whoisClient.canResolveAddress(cidr)) {
			resource = whoisClient.resolveAddress(cidr);
			
			if(resource != null) {
				rsplService.persistNetworkResource(resource);

				logger.info("looked of IP Address {} yielded  WHOIS lookup result {}", cidr, resource);
				
				return new ResponseEntity<NetworkResource>(resource, HttpStatus.OK);
			}			
		}
		
		return new ResponseEntity<NetworkResource>(HttpStatus.NOT_FOUND);
	}
}
