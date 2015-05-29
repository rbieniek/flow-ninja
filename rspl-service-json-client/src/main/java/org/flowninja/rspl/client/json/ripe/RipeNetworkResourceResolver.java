/**
 * 
 */
package org.flowninja.rspl.client.json.ripe;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.flowninja.rspl.client.json.common.IJsonNetworkResourceResolver;
import org.flowninja.rspl.client.json.common.RegistryResponseHandler;
import org.flowninja.rspl.definitions.services.INetworkResourceResolver;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.rspl.definitions.types.ResultDocument;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resolving network information according to this URI: http://rest.db.ripe.net/search.json?query-string=a.b.c.d&type-filter=inetnum&flags=resource
 * 
 * This is the RESTFul service provided by ARIN
 * 
 * @author rainer
 *
 */
@Component
public class RipeNetworkResourceResolver implements IJsonNetworkResourceResolver {
	private static final Logger logger = LoggerFactory.getLogger(RipeNetworkResourceResolver.class);
	
	private Set<CIDR4Address> prefixes = new HashSet<CIDR4Address>();
	
	@Autowired
	private CloseableHttpClient httpClient;
	
	@Autowired
	private RegistryResponseHandler responseHandler;

	@Autowired
	private RIPEResultDocumentProcessor processor;

	public RipeNetworkResourceResolver() {
		prefixes.add(new CIDR4Address(new byte[] {(byte)2, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)5, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)5, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)31, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)37, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)46, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)51, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)53, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)57, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)62, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)77, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)78, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)79, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)80, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)81, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)82, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)83, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)84, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)85, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)86, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)87, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)88, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)89, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)90, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)91, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)92, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)93, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)94, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)95, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)109, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)141, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)145, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)151, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)176, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)178, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)185, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)188, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)193, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)194, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)195, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)212, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)213, 0, 0, 0}, 8));
		prefixes.add(new CIDR4Address(new byte[] {(byte)217, 0, 0, 0}, 8));	
	}
	
	@Override
	public ResultDocument resolveNetworkAddress(byte[] networkAddress) throws URISyntaxException, IOException{
		ResultDocument result = null;
		String ipAddr = NetworkAddressHelper.formatAddressSpecification(networkAddress);
		URI uri = (new URIBuilder())
				.setScheme("http")
				.setHost("rest.db.ripe.net")
				.setPath("/search.json")
				.setParameter("query-string", ipAddr)
				.setParameter("type-filter", "inetnum")
				.setParameter("flags", "resource")
				.build();
		
		logger.info("resolving address {} with URI {}", ipAddr, uri);
		
		result = processor.processResultDocument(httpClient.execute(RequestBuilder.get(uri)
					.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
					.setHeader(HttpHeaders.ACCEPT_CHARSET, ContentType.APPLICATION_JSON.getCharset().name())
					.build(), 
				responseHandler));

		logger.info("resolving address {} with URI {} yielded {}", ipAddr, uri, result);

		return result;
	}

	@Override
	public ENetworkRegistry resolvingRegistry() {
		return ENetworkRegistry.RIPE;
	}

	@Override
	public boolean canResolveAddress(byte[] networkAddress) {
		return (prefixes.stream().filter((n) -> n.isInRange(networkAddress)).count() > 0);
	}

}
