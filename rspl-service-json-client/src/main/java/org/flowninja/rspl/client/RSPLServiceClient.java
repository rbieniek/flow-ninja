/**
 * 
 */
package org.flowninja.rspl.client;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.flowninja.rspl.client.common.INetworkResourceResolver;
import org.flowninja.rspl.client.common.ResolveResult;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class RSPLServiceClient implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(RSPLServiceClient.class);
	
	@Autowired
	private Set<INetworkResourceResolver> resolvers;

	private Map<ENetworkRegistry, INetworkResourceResolver> resolverMap = new HashMap<>();
	
	public NetworkResource resolveAddress(byte[] address) {
		NetworkResource resource = null;
		Deque<ENetworkRegistry> registries = new LinkedList<ENetworkRegistry>(resolverMap.keySet());
		
		logger.info("resolving network address: {}", address);
		
		while(!registries.isEmpty()) {
			ResolveResult result = resolverMap.get(registries.pop()).resolveNetworkAddress(address);

			if(result != null) {
				if(result.getResource() != null) {
					resource = result.getResource();
					break;
				} else if(result.getReferredRegistry() != null) {
					// leave processing loop if being referred to a RIR which is not supported or has already been tried 
					if(!registries.contains(result.getReferredRegistry()))
						break;

					// put referred registry first in line
					registries.remove(result.getReferredRegistry());
					registries.push(result.getReferredRegistry());
				}
			}
		}

		logger.info("resolving network address {} yielded {}", address, resource);
		
		return resource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for(INetworkResourceResolver resolver : resolvers) {
			logger.info("found network address resolver for registry {}", resolver.resolvingRegistry());
			
			resolverMap.put(resolver.resolvingRegistry(), resolver);
		}
	}

}
