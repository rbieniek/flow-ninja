/**
 * 
 */
package org.flowninja.rspl.client.json.ripe;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.rspl.client.json.TestConfig;
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
public class RipeNetworkResourceResolverTest {

	private static final byte[] IP_ADDR_BLIZZARD_FR = new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x1a };
	private static final byte[] IP_ADDR_WWW_GOOGLE_COM = new byte[] { (byte)0xd8, (byte)0x4e, (byte)0xd3, (byte)0x04 };
	
	@Autowired
	private RipeNetworkResourceResolver resolver;

	@Test
	public void canResolve() {
		assertThat(resolver.canResolveAddress(IP_ADDR_BLIZZARD_FR)).isTrue();
	}
	
	@Test
	public void cannetResolve() {
		assertThat(resolver.canResolveAddress(IP_ADDR_WWW_GOOGLE_COM)).isFalse();
	}
	
	@Test
	public void resolveBlizzardFR() throws Exception {
		assertThat(resolver.resolveNetworkAddress(IP_ADDR_BLIZZARD_FR))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_BLIZZARD_FR, 24), "FR-BLIZZARD", "FR", ENetworkRegistry.RIPE));
	}
	
	@Test
	public void resolveWwwGoogleCom() throws Exception {
		assertThat(resolver.resolveNetworkAddress(IP_ADDR_WWW_GOOGLE_COM)).isNull();
	}

	@Test
	public void resolveUnknown() throws Exception {
		assertThat(resolver.resolveNetworkAddress(new byte[] {(byte)0xf5, (byte)0x02, (byte)0x03, (byte)0x04})).isNull();
	}

}
