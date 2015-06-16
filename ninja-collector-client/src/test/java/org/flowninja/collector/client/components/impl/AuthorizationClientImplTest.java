/**
 * 
 */
package org.flowninja.collector.client.components.impl;

import static org.fest.assertions.api.Assertions.*;

import java.net.URI;

import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.flowninja.collector.client.TestConfiguration;
import org.flowninja.collector.client.auth.BearerCredentials;
import org.flowninja.collector.client.components.BearerToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class AuthorizationClientImplTest {

	@Autowired
	private CloseableHttpClient httpClient;
	
	@Autowired
	private Server httpServer;
	
	@Autowired
	private ServerConnector serverConnector;
	
	private AuthorizationClientImpl client;
	private BearerCredentials credentials;
	
	@Before
	public void before() throws Exception {
		this.client = new AuthorizationClientImpl(httpClient);
		
		httpServer.start();
		
		this.credentials = new BearerCredentials("foo", "bar", 
				new URI("http", null, "localhost", serverConnector.getLocalPort(), "/oauth2/token", 
				null, null));
	}
	
	@After
	public void after() throws Exception {
		httpServer.stop();
	}
	
	@Test
	public void obtainToken() throws Exception {
		BearerToken token = client.grantClientCredentials(credentials, null);
		
		assertThat(token).isNotNull();
		assertThat(token.getToken()).isNotNull();
		assertThat(token.getTimeToLive()).isNotNull();
	}
}
