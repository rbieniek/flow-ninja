/**
 * 
 */
package org.flowninja.rspl.client.json.arin;

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
public class ARINNetworkResourceResolverTest {

	private static final byte[] IP_ADDR_BLIZZARD_FR = new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x1a };
	private static final byte[] IP_ADDR_WWW_GOOGLE_COM = new byte[] { (byte)0xd8, (byte)0x3a, (byte)0xd3, (byte)0x04 };
	private static final byte[] IP_ADDR_STATIC_AKAMAI_COM = new byte[] { (byte)0xac, (byte)0xe3, (byte)0xa4, (byte)0x70 };
	
	@Autowired
	private ArinNetworkResourceResolver resolver;
	
	@Test
	public void cannotResolve() {
		assertThat(resolver.canResolveAddress(IP_ADDR_BLIZZARD_FR)).isFalse();
	}
	
	@Test
	public void canResolve() {
		assertThat(resolver.canResolveAddress(IP_ADDR_WWW_GOOGLE_COM)).isTrue();
	}
	
	@Test
	public void resolveWwwGoogleCom() throws Exception {
		assertThat(resolver.resolveNetworkAddress(IP_ADDR_WWW_GOOGLE_COM))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_GOOGLE_COM, 19), "GOOGLE", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void resolveStaticAkamaiCom() throws Exception {
		assertThat(resolver.resolveNetworkAddress(IP_ADDR_STATIC_AKAMAI_COM))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_STATIC_AKAMAI_COM, 12), "AKAMAI", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void resolveBlizzardFR() throws Exception {
		assertThat(resolver.resolveNetworkAddress(IP_ADDR_BLIZZARD_FR)).isNull();
	}

	@Test
	public void resolveUnknown() throws Exception {
		assertThat(resolver.resolveNetworkAddress(new byte[] {(byte)0xf5, (byte)0x02, (byte)0x03, (byte)0x04})).isNull();
	}

}
