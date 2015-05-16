/**
 * 
 */
package org.flowninja.rspl.client.whois;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.flowninja.rspl.definitions.services.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class RSPLServiceWhoisClient {
	private static final Logger logger = LoggerFactory.getLogger(RSPLServiceWhoisClient.class);
	
	@Autowired
	private Set<INetworkResourceResolver> resolvers;
	
	public NetworkResource resolveAddress(byte[] address) {
		NetworkResource resource = null;
		
		logger.info("resolving network address: {}", address);

		for(INetworkResourceResolver resolver : resolvers) {
			if(resolver.canResolveAddress(address)) {
				logger.info("resolving network address {} in registry {}", address, resolver.resolvingRegistry());
				
				try {
					resource = resolver.resolveNetworkAddress(address);
		
				} catch(URISyntaxException e) {
					logger.error("failed to resolve network address with registry {}", resolver.resolvingRegistry(), e);
				} catch(IOException e) {
					logger.error("failed to resolve network address with registry {}", resolver.resolvingRegistry(), e);				
				}				
			}
		}
		
		logger.info("resolving network address {} yielded {}", address, resource);
		
		return resource;
	}

	public boolean canResolveAddress(byte[] address) {
		logger.info("check if network address can be resolved: {}", address);

		boolean resolvable = (resolvers.stream().filter((n) -> n.canResolveAddress(address)).count() > 0);

		logger.info("check if network address {} can be resolved yielded {}", address, resolvable);

		return resolvable;
	}

}
