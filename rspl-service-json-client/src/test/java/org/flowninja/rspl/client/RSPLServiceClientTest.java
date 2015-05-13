/**
 * 
 */
package org.flowninja.rspl.client;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
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
public class RSPLServiceClientTest {
	private static final byte[] IP_ADDR_BLIZZARD_FR = new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x1a };
	private static final byte[] IP_ADDR_WWW_GOOGLE_COM = new byte[] { (byte)0xd8, (byte)0x4a, (byte)0xd3, (byte)0x04 };

	@Autowired
	private RSPLServiceClient client;
	
	@Test
	public void resolveBlizzardFR() throws Exception {
		assertThat(client.resolveAddress(IP_ADDR_BLIZZARD_FR))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_BLIZZARD_FR, 24), "FR-BLIZZARD", "FR", ENetworkRegistry.RIPE));
	}
}
