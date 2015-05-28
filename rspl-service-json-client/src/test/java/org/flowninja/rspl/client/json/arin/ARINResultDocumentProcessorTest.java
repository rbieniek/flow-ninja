/**
 * 
 */
package org.flowninja.rspl.client.json.arin;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.rspl.client.json.TestConfig;
import org.flowninja.rspl.client.json.ripe.RIPEResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class ARINResultDocumentProcessorTest {
	private static final byte[] IP_ADDR_WWW_GOOGLE_COM = new byte[] { (byte)0xad, (byte)0xc2, (byte)0xd3, (byte)0x04 };
	private static final byte[] IP_ADDR_STATIC_AKAMAI_COM = new byte[] { (byte)0xac, (byte)0xe3, (byte)0xa4, (byte)0x70 };
	private static final byte[] IP_ADDR_WWW_NDR_DE = new byte[] { (byte)0x17, (byte)0x39, (byte)0x6d, (byte)0xa3 };

	@Autowired
	private ResourceLoader loader;
	
	@Autowired
	private ARINResultDocumentProcessor processor;
	
	private JSONObject arinFound;
	private JSONObject arinOther;
	private JSONObject arinNotFound;
	private JSONObject akamaiFound;
	private JSONObject akamaiReassigned;
	
	@Before
	public void before() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new JsonOrgModule());
		
		arinFound = mapper.readValue(loader.getResource("classpath:arin-found.json").getInputStream(), JSONObject.class);
		arinOther = mapper.readValue(loader.getResource("classpath:arin-other.json").getInputStream(), JSONObject.class);
		arinNotFound = mapper.readValue(loader.getResource("classpath:arin-not-found.json").getInputStream(), JSONObject.class);
		akamaiFound = mapper.readValue(loader.getResource("classpath:akamai-found.json").getInputStream(), JSONObject.class);
		akamaiReassigned = mapper.readValue(loader.getResource("classpath:akamai-reassigned.json").getInputStream(), JSONObject.class);
	}

	
	@Test
	public void arinFound() {
		assertThat(processor.processResultDocument(arinFound))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_GOOGLE_COM, 16), "GOOGLE", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void akamaiFound() {
		assertThat(processor.processResultDocument(akamaiFound))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_STATIC_AKAMAI_COM, 12), "AKAMAI", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void akamaiReassigned() {
		assertThat(processor.processResultDocument(akamaiReassigned))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_NDR_DE, 20), "AIBV", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void arinOther() {
		assertThat(processor.processResultDocument(arinOther)).isNull();
	}

	@Test
	public void arinNotFound() {
		assertThat(processor.processResultDocument(arinNotFound)).isNull();
	}

}
