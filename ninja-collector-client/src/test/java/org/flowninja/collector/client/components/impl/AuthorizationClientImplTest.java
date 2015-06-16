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
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
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
	private ApplicationContext ctx;
	
	@Autowired
	private Server httpServer;
	
	@Autowired
	private ServerConnector serverConnector;
	
	private AuthorizationClientImpl client;
	private BearerCredentials unscopedCredentials;
	private BearerCredentials scopedCredentials;
	
	@Before
	public void before() throws Exception {
		this.client = new AuthorizationClientImpl(ctx.getBean(CloseableHttpClient.class));
		
		httpServer.start();
		
		this.unscopedCredentials = new BearerCredentials("foo", "bar", 
				new URI("http", null, "localhost", serverConnector.getLocalPort(), "/oauth/token", 
				null, null));
		this.scopedCredentials = new BearerCredentials("foofoo", "barbar", 
				new URI("http", null, "localhost", serverConnector.getLocalPort(), "/oauth/token", 
				null, null));
	}
	
	@After
	public void after() throws Exception {
		httpServer.stop();
	}
	
	@Test
	public void obtainTokenWithoutScope() throws Exception {
		BearerToken token = client.grantClientCredentials(unscopedCredentials, null);
		
		assertThat(token).isNotNull();
		assertThat(token.getToken()).isNotNull();
		assertThat(token.getTimeToLive()).isNotNull();
	}
	
	@Test
	public void obtainTokenWithScope() throws Exception {
		BearerToken token = client.grantClientCredentials(scopedCredentials, "restricted");
		
		assertThat(token).isNotNull();
		assertThat(token.getToken()).isNotNull();
		assertThat(token.getTimeToLive()).isNotNull();
	}
}
