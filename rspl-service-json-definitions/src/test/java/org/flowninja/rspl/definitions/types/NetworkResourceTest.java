/**
 * 
 */
package org.flowninja.rspl.definitions.types;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;

/**
 * @author rainer
 *
 */
public class NetworkResourceTest {
	@Test
	public void equality() {
		NetworkResource res1 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		NetworkResource res2 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		
		assertThat(res1).isEqualTo(res2);
		assertThat(res1.hashCode()).isEqualTo(res2.hashCode());
	}

	@Test
	public void inequalityCIDR() {
		NetworkResource res1 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x80, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		NetworkResource res2 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		
		assertThat(res1).isNotEqualTo(res2);
		assertThat(res1.hashCode()).isNotEqualTo(res2.hashCode());
	}

	@Test
	public void inequalityNetname() {
		NetworkResource res1 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		NetworkResource res2 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"bar-foo", "DE", ENetworkRegistry.RIPE);
		
		assertThat(res1).isNotEqualTo(res2);
		assertThat(res1.hashCode()).isNotEqualTo(res2.hashCode());
	}

	@Test
	public void inequalityCountry() {
		NetworkResource res1 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		NetworkResource res2 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "US", ENetworkRegistry.RIPE);
		
		assertThat(res1).isNotEqualTo(res2);
		assertThat(res1.hashCode()).isNotEqualTo(res2.hashCode());
	}

	@Test
	public void inequalityRIR() {
		NetworkResource res1 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.RIPE);
		NetworkResource res2 = new NetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x40, (byte)0x21 }, 24), 
				"foo-bar", "DE", ENetworkRegistry.APNIC);
		
		assertThat(res1).isNotEqualTo(res2);
		assertThat(res1.hashCode()).isNotEqualTo(res2.hashCode());
	}
}
