/**
 * 
 */
package org.flowninja.rspl.client.json.arin;

import static org.fest.assertions.api.Assertions.assertThat;

import org.flowninja.rspl.client.json.TestConfig;
import org.flowninja.rspl.client.json.ripe.RIPEResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.CIDR4Address;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
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

	@Autowired
	private ResourceLoader loader;
	
	@Autowired
	private ARINResultDocumentProcessor processor;
	
	private JSONObject arinFound;
	private JSONObject arinOther;
	private JSONObject arinNotFound;
	
	@Before
	public void before() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new JsonOrgModule());
		
		arinFound = mapper.readValue(loader.getResource("classpath:arin-found.json").getInputStream(), JSONObject.class);
		arinOther = mapper.readValue(loader.getResource("classpath:arin-other.json").getInputStream(), JSONObject.class);
		arinNotFound = mapper.readValue(loader.getResource("classpath:arin-not-found.json").getInputStream(), JSONObject.class);
	}

	
	@Test
	public void ripeFound() {
		assertThat(processor.processResultDocument(arinFound))
			.isEqualTo(new NetworkResource(new CIDR4Address(IP_ADDR_WWW_GOOGLE_COM, 16), "GOOGLE", null, ENetworkRegistry.ARIN));
	}

	@Test
	public void ripeOther() {
		assertThat(processor.processResultDocument(arinOther)).isNull();
	}

	@Test
	public void ripeNotFound() {
		assertThat(processor.processResultDocument(arinNotFound)).isNull();
	}

}
