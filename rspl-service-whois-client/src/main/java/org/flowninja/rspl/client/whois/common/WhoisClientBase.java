/**
 * 
 */
package org.flowninja.rspl.client.whois.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.net.whois.WhoisClient;
import org.flowninja.rspl.definitions.NetworkAddressHelper;
import org.flowninja.rspl.definitions.services.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author rainer
 *
 */
public abstract class WhoisClientBase implements INetworkResourceResolver {
	private static final Logger logger = LoggerFactory.getLogger(WhoisClientBase.class);

	protected Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();
	
	@Autowired
	private WhoisStreamParser whoisParser;

	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.common.INetworkResourceResolver#canResolveAddress(byte[])
	 */
	public final boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

	@Override
	public NetworkResource resolveNetworkAddress(byte[] networkAddress) throws URISyntaxException, IOException {
		NetworkResource resource = null;
		WhoisClient client = new WhoisClient();
		
		try {
			client.connect(whoisHost());
			
			List<IWhoisRecord> records = whoisParser.parseWhoisResponse(client.getInputStream(NetworkAddressHelper.formatAddressSpecification(networkAddress)));
			
			for(IWhoisRecord record : records) {
				if(record instanceof InetnumRecord) {
					resource = mapInetNumRecord((InetnumRecord)record);
					
					break;
				}
			}
		} catch(IOException e) {
			logger.error("exception while interacting with remote WHOIS server", e);
		} finally {
			try {
				client.disconnect();
			} catch(IOException e) {
				logger.warn("exception while closing connection to remote WHOIS server", e);				
			}
		}
		
		return resource;
	}

	/**
	 * Map the inetnum record to a network resource
	 * 
	 * @param record
	 * @return
	 */
	protected abstract NetworkResource mapInetNumRecord(InetnumRecord record);
	
	/**
	 * Get the hostname of the WHOIS server responsible for that RIR
	 * @return
	 */
	protected abstract String whoisHost();
}
