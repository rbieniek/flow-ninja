/**
 * 
 */
package org.flowninja.rspl.client.rdap;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.rspl.client.rdap.common.RdapPayloadResponseHandler;
import org.flowninja.rspl.client.rdap.common.RdapRedirectStrategy;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RSPLServiceRdapClientTest.Config.class)
public class RSPLServiceRdapClientTest {

	private static final byte[] IP_ADDR_WWW_LACNIC_NET = new byte[] { (byte)0xc8, (byte)0x03, (byte)0x0e, (byte)0xa7 };
	private static final byte[] IP_ADDR_WWW_APNIC_NET = new byte[] { (byte)0xcb, (byte)0x77, (byte)0x66, (byte)0xf4 };
	
	@Configuration
	public static class Config {
		@Bean
		public RSPLServiceRdapClient client() {
			return new RSPLServiceRdapClient();
		}
		
		@Bean
		public RdapRedirectStrategy forwardingResponseHandler() {
			return new RdapRedirectStrategy();
		}
		
		@Bean
		public RdapPayloadResponseHandler payloadResponseHandler() {
			return new RdapPayloadResponseHandler();
		}
	}
	
	@Autowired
	private RSPLServiceRdapClient client;
	
	@Test
	public void resolveWwwLacnicNet() {
		assertThat(client.resolveAddress(IP_ADDR_WWW_LACNIC_NET))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_LACNIC_NET, 32), 
					"LACNIC - Latin American and Caribbean IP address", 
					"UY", 
					ENetworkRegistry.LACNIC));
	}
	
	@Test
	public void resolveWwwApnicNet() {
		assertThat(client.resolveAddress(IP_ADDR_WWW_APNIC_NET))
		.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_APNIC_NET, 20), 
				"APNIC-SERVICES", 
				"AU", 
				ENetworkRegistry.APNIC));		
	}
}
