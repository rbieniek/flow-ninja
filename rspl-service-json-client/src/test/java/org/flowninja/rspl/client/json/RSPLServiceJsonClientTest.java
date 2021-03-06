/**
 * 
 */
package org.flowninja.rspl.client.json;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.rspl.client.json.RSPLServiceJsonClient;
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
public class RSPLServiceJsonClientTest {
	private static final byte[] IP_ADDR_BLIZZARD_FR = new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x1a };
	private static final byte[] IP_ADDR_WWW_GOOGLE_COM = new byte[] { (byte)0xd8, (byte)0x3a, (byte)0xd3, (byte)0x04 };
	private static final byte[] IP_ADDR_AP_NIC_NET = new byte[] { (byte)0xca, (byte)0x0c, (byte)0x1d, (byte)0xaf };
	private static final byte[] IP_ADDR_TECTRUM24_DE = new byte[] { (byte)0xC0, (byte)0x36, (byte)0x2d, (byte)0x09 };

	@Autowired
	private RSPLServiceJsonClient client;
	
	@Test
	public void resolveBlizzardFR() throws Exception {
		assertThat(client.resolveAddress(IP_ADDR_BLIZZARD_FR))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_BLIZZARD_FR, 24), "FR-BLIZZARD", "FR", ENetworkRegistry.RIPE));
	}
	
	@Test
	public void canResolveBlizzardFR() {
		assertThat(client.canResolveAddress(IP_ADDR_BLIZZARD_FR)).isTrue();
	}

	@Test
	public void resolveTectrumDE() throws Exception {
		assertThat(client.resolveAddress(IP_ADDR_TECTRUM24_DE))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_TECTRUM24_DE, 24), "IMD-TECTRUM", "DE", ENetworkRegistry.RIPE));
	}

	@Test
	public void canResolveTectrumDE() {
		assertThat(client.canResolveAddress(IP_ADDR_TECTRUM24_DE)).isTrue();
	}

	@Test
	public void resolveWwwGoogleCom() throws Exception {
		assertThat(client.resolveAddress(IP_ADDR_WWW_GOOGLE_COM))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_GOOGLE_COM, 19), "GOOGLE", null, ENetworkRegistry.ARIN));
	}
	
	@Test
	public void canResolveWwwGoogleCom() {
		assertThat(client.canResolveAddress(IP_ADDR_WWW_GOOGLE_COM)).isTrue();
	}

	@Test
	public void cannotResolveApnicNet() {
		assertThat(client.canResolveAddress(IP_ADDR_AP_NIC_NET)).isFalse();
	}
}
