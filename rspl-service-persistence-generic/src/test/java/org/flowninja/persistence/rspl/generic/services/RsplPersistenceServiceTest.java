/**
 * 
 */
package org.flowninja.persistence.rspl.generic.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.flowninja.persistence.rspl.generic.types.NetworkInformation;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
@ContextConfiguration(classes=RsplPersistenceServiceTest.Config.class)
public class RsplPersistenceServiceTest {

	private static final CIDR4Address NET_192_168_0_0 = new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x00 }, 16);
	
	@Configuration
	public static class Config {
		@Bean
		public RsplPersistenceService persistenceService() {
			return new RsplPersistenceService();
		}
		
		@Bean
		public INetworkResourceLoadService loadService() {
			INetworkResourceLoadService loadService = Mockito.mock(INetworkResourceLoadService.class);
			
			when(loadService.readNetworkInformation(NET_192_168_0_0)).thenReturn(new NetworkInformation("foo", "bar", ENetworkRegistry.RIPE));
			
			return loadService;
		}
		
		@Bean
		public INetworkResourcePersistService persistService() {
			return Mockito.mock(INetworkResourcePersistService.class);
		}
	}
	
	@Autowired
	private RsplPersistenceService service;
	
	@Test
	public void findNet() {
		assertThat(service.loadNetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x30, (byte)0x04 })))
			.isEqualTo(new NetworkResource(NET_192_168_0_0, "foo", "bar", ENetworkRegistry.RIPE));
	}

	@Test
	public void notFound() {
		assertThat(service.loadNetworkResource(new CIDR4Address(new byte[] { (byte)0xac, (byte)0x10, (byte)0x30, (byte)0x04 }))).isNull();
	}

	@Test
	public void ignoreNonHost() {
		assertThat(service.loadNetworkResource(new CIDR4Address(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x30, (byte)0x04 }, 28))).isNull();
	}
}
