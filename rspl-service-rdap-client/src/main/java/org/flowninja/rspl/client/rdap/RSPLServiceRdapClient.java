/**
 * 
 */
package org.flowninja.rspl.client.rdap;

import java.io.IOError;
import java.net.URI;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.flowninja.rspl.client.rdap.common.RdapPayloadResponseHandler;
import org.flowninja.rspl.client.rdap.common.RdapRedirectStrategy;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rainer
 *
 */
@Service
public class RSPLServiceRdapClient implements InitializingBean, DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(RSPLServiceRdapClient.class);
	
	private PoolingHttpClientConnectionManager connectionManager;
	
	@Autowired
	private RdapRedirectStrategy redirectStrategy;
	
	@Autowired
	private RdapPayloadResponseHandler payloadResponseHandler;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		logger.info("starting HTTP client provider");

		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
	}

	@Override
	public void destroy() throws Exception {
		logger.info("stopping HTTP client provider");
		
		try {
		connectionManager.close();
		} catch(IOError e) {
			logger.error("problems closing HTTP client connection manager", e);
		}
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	public NetworkResource resolveAddress(byte[] address) {
		NetworkResource resource = null;
		CloseableHttpClient httpClient = null;
		String ipAddr = NetworkAddressHelper.formatAddressSpecification(address);
		
		logger.info("resolving network address: {}", ipAddr);

		try {
			URI uri = (new URIBuilder())
					.setScheme("http")
					.setHost("rdap.org")
					.setPath("/ip/" + ipAddr)
					.build();

			httpClient = httpClient();
			
			logger.info("using URI {} for looking up address {}", uri, ipAddr);
			
			resource = httpClient.execute(RequestBuilder.get(uri)
					.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
					.setHeader(HttpHeaders.ACCEPT_CHARSET, ContentType.APPLICATION_JSON.getCharset().name())
					.build(), 
					payloadResponseHandler);
				
		} catch(Exception e) {
			logger.warn("problems retrieving RDAP information for address {}", ipAddr, e);			
		} finally {
			try {
			if(httpClient != null)
				httpClient.close();
			} catch(Exception e) {
				logger.warn("problems relasing HTTP client for address {}", ipAddr, e);
			}
		}
		
		logger.info("resolved network address {} to resource {}", ipAddr, resource);
		
		return resource;
	}
	
	private CloseableHttpClient httpClient() throws Exception {
		return HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setConnectionManagerShared(true)
				.setRedirectStrategy(redirectStrategy)
				.build();
	}
}
