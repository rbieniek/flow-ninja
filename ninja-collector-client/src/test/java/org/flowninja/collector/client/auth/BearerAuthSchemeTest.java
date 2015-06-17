/**
 * 
 */
package org.flowninja.collector.client.auth;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;

import javax.json.JsonObject;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.flowninja.collector.client.BearerHttpClientContextBuilder;
import org.flowninja.collector.client.TestConfiguration;
import org.flowninja.collector.client.components.impl.DefaultAuthorizationClientFactory;
import org.flowninja.collector.client.components.impl.rest.RestClientDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class BearerAuthSchemeTest {
	private static final Logger logger = LoggerFactory.getLogger(BearerAuthSchemeTest.class);

	private static class ClientDetailsResponseHandler implements ResponseHandler<RestClientDetails> {

		private ObjectMapper mapper;
		
		private ClientDetailsResponseHandler() {
			mapper = new ObjectMapper();
			mapper.registerModule(new JSR353Module());
		}
		
		@Override
		public RestClientDetails handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			RestClientDetails clientDetails = null;

			logger.info("obtaining client credentials yielded {}", response.getStatusLine());
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JsonObject json = mapper.readValue(response.getEntity().getContent(), JsonObject.class);
				
				clientDetails = new RestClientDetails(json.getString("token", null), json.getString("type", null));
			}
			
			return clientDetails;
		}
		
	}
	
	@Autowired
	private Server httpServer;
	
	@Autowired
	private ServerConnector serverConnector;
	
	private BearerCredentials unscopedCredentials;
	private BearerCredentials scopedCredentials;
	private URI clientDetailsURI;
	private CloseableHttpClient httpClient;
	private BearerSchemeFactory schemeFactory;
	
	@Before
	public void before() throws Exception {
		httpServer.start();
		
		this.unscopedCredentials = new BearerCredentials("foo", "bar", 
				new URI("http", null, "localhost", serverConnector.getLocalPort(), "/oauth/token", 
				null, null));
		this.scopedCredentials = new BearerCredentials("foofoo", "barbar", 
				new URI("http", null, "localhost", serverConnector.getLocalPort(), "/oauth/token", 
				null, null));
		
		clientDetailsURI = new URI("http", null, "localhost", serverConnector.getLocalPort(), "/rest/client-details", 
				null, null);
		
		httpClient = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
		
		schemeFactory = new BearerSchemeFactory(new DefaultAuthorizationClientFactory(new PoolingHttpClientConnectionManager()));
	}
	
	@After
	public void after() throws Exception {
		httpServer.stop();
	}
	
	@Test
	public void readClientCredentialsWithoutScope() throws Exception {
		HttpGet httpGet = new HttpGet(clientDetailsURI);
		HttpClientContext context = BearerHttpClientContextBuilder.create()
				.withBearerCredentials(new AuthScope(serverConnector.getHost(),serverConnector.getLocalPort()), unscopedCredentials)
				.withBearerSchemeFactory(schemeFactory)
				.build();
		
		httpGet.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		
		RestClientDetails details = httpClient.execute(httpGet, new ClientDetailsResponseHandler(), context);
		
		assertThat(details).isNotNull();
		assertThat(details.getToken()).isNotNull();
		assertThat(details.getType()).isEqualTo("Bearer");
	}
	
	@Test
	public void readClientCredentialsWithScope() throws Exception {
		HttpGet httpGet = new HttpGet(clientDetailsURI);
		HttpClientContext context = BearerHttpClientContextBuilder.create()
				.withBearerCredentials(new AuthScope(serverConnector.getHost(),serverConnector.getLocalPort()), scopedCredentials)
				.withBearerSchemeFactory(schemeFactory)
				.build();
		
		httpGet.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

		RestClientDetails details = httpClient.execute(httpGet, new ClientDetailsResponseHandler(), context);
		
		assertThat(details).isNotNull();
		assertThat(details.getToken()).isNotNull();
		assertThat(details.getType()).isEqualTo("Bearer");
	}
	
	
}
