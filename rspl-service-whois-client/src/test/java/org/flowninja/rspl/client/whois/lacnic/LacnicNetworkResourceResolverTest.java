/**
 * 
 */
package org.flowninja.rspl.client.whois.lacnic;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.rspl.client.whois.TestConfig;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
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
@ContextConfiguration(classes=TestConfig.class)
public class LacnicNetworkResourceResolverTest {

	private static final byte[] WWW_AFRINIC_NET = new byte[] { (byte)0xc4, (byte)0xd8, (byte)0x02, (byte)0x06 };
	private static final byte[] WWW_APNIC_NET = new byte[] { (byte)0xcb, (byte)0x77, (byte)0x66, (byte)0xf4 };
	private static final byte[] WWW_LACNIC_NET = new byte[] { (byte)0xc8, (byte)0x03, (byte)0x0e, (byte)0x93 };
	
	@Autowired
	private LacnicNetworkResourceResolver resolver;
	
	@Test
	public void canResolve() {
		assertThat(resolver.canResolveAddress(WWW_LACNIC_NET)).isTrue();
	}

	@Test
	public void cannotResolve() {
		assertThat(resolver.canResolveAddress(WWW_APNIC_NET)).isFalse();
		assertThat(resolver.canResolveAddress(WWW_AFRINIC_NET)).isFalse();
	}

	@Test
	public void resolve() throws Exception {
		assertThat(resolver.resolveNetworkAddress(WWW_LACNIC_NET))
			.isEqualTo(new NetworkResource(new CIDR4Address(WWW_LACNIC_NET, 22), 
					"LACNIC - Latin American and Caribbean IP address", 
					null, 
					ENetworkRegistry.LACNIC));
	}
}
