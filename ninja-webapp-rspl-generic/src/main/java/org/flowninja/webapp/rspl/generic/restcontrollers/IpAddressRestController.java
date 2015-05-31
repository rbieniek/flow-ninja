/**
 * 
 */
package org.flowninja.webapp.rspl.generic.restcontrollers;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService;
import org.flowninja.persistence.rspl.generic.services.RsplPersistenceService;
import org.flowninja.rspl.client.json.RSPLServiceJsonClient;
import org.flowninja.rspl.client.rdap.RSPLServiceRdapClient;
import org.flowninja.rspl.client.whois.RSPLServiceWhoisClient;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.statistics.generic.services.IRpslStatisticsService;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private RSPLServiceRdapClient rdapClient;
	
	@Autowired
	private IRpslStatisticsService statisticsService;
	
	@RequestMapping(produces="application/json", method=RequestMethod.GET, value="/rest/ip/address")
	public Callable<ResponseEntity<NetworkResourceResult>> ipAddr(@RequestParam(value="ipv4", required=false) final String ipAddr) {
		return new Callable<ResponseEntity<NetworkResourceResult>>() {
			
			@Override
			public ResponseEntity<NetworkResourceResult> call() throws Exception {
				try {
					CIDR4Address cidr;
					
					statisticsService.recordLookupRequest();
					
					if(StringUtils.isBlank(ipAddr)) {
						logger.info("No IPv4 address given");
						
						statisticsService.recordBadRequest();
						
						return new ResponseEntity<NetworkResourceResult>(HttpStatus.BAD_REQUEST);
					}
					
					logger.info("looking up IP address: '{}'", ipAddr);
					
					try {
						cidr = new CIDR4Address(NetworkAddressHelper.parseAddressSpecification(ipAddr));
					} catch(IllegalArgumentException e) {
						logger.info("failed to parse passed address specification", e);
						
						statisticsService.recordBadRequest();
						
						return new ResponseEntity<NetworkResourceResult>(HttpStatus.BAD_REQUEST);
					}
					
					if(blockednetworkService.findContainingBlockedNetwork(cidr) != null) {
						logger.info("IP address {} is in adminstratively blocked network range", cidr);

						statisticsService.recordAdminsitrativeBlocked();
						
						return new ResponseEntity<NetworkResourceResult>(HttpStatus.NOT_FOUND);
					}
			
					NetworkResource resource = rsplService.loadNetworkResource(cidr);
					
					if(resource != null) {
						logger.info("looked of IP Address {} yielded cache lookup result {}", cidr, resource);
						
						statisticsService.recordResultFromCache();
						
						return new ResponseEntity<NetworkResourceResult>(new NetworkResourceResult(resource, EResultSource.CACHE), HttpStatus.OK);
					}

					if((resource = rdapClient.resolveAddress(cidr)) != null) { 
						rsplService.persistNetworkResource(resource);
						
						logger.info("looked of IP Address {} yielded JSON RDAP lookup result {}", cidr, resource);
						
						statisticsService.recordResultFromRdapService();
		
						return new ResponseEntity<NetworkResourceResult>(new NetworkResourceResult(resource, EResultSource.JSON), HttpStatus.OK);					
					} else if(jsonClient.canResolveAddress(cidr)) {
						resource = jsonClient.resolveAddress(cidr);
						
						if(resource != null) {
							rsplService.persistNetworkResource(resource);
			
							logger.info("looked of IP Address {} yielded JSON WHOIS lookup result {}", cidr, resource);
							
							statisticsService.recordResultFromJsonService();
			
							return new ResponseEntity<NetworkResourceResult>(new NetworkResourceResult(resource, EResultSource.JSON), HttpStatus.OK);
						}
					} else if(whoisClient.canResolveAddress(cidr)) {
						resource = whoisClient.resolveAddress(cidr);
						
						if(resource != null) {
							rsplService.persistNetworkResource(resource);
			
							logger.info("looked of IP Address {} yielded  WHOIS lookup result {}", cidr, resource);
							
							statisticsService.recordResultFromWhoisService();
							
							return new ResponseEntity<NetworkResourceResult>(new NetworkResourceResult(resource, EResultSource.WHOIS), HttpStatus.OK);
						}			
					}

					statisticsService.recordNotFound();
					
					return new ResponseEntity<NetworkResourceResult>(HttpStatus.NOT_FOUND);
				} catch(DataAccessException e) {
					logger.error("Failed to access persistence modules", e);
					
					return new ResponseEntity<NetworkResourceResult>(HttpStatus.SERVICE_UNAVAILABLE);			
				} catch(Exception e) {
					logger.error("Exception caught while processing the request", e);
					
					return new ResponseEntity<NetworkResourceResult>(HttpStatus.INTERNAL_SERVER_ERROR);				
				}
			}
		};
		
	}
}
