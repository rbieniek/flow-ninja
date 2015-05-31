/**
 * 
 */
package org.flowninja.rspl.client.json.ripe;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.rspl.client.json.TestConfig;
import org.flowninja.rspl.client.json.ripe.RIPEResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.rspl.definitions.types.ResultDocument;
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
public class RIPEResultDocumentProcessorTest {

	private static final byte[] IP_ADDR_BLIZZARD_FR = new byte[] { (byte)0x50, (byte)0xef, (byte)0xba, (byte)0x1a };

	@Autowired
	private ResourceLoader loader;
	
	@Autowired
	private RIPEResultDocumentProcessor processor;
	
	private JSONObject ripeFound;
	private JSONObject ripeOther;
	private JSONObject ripeNotFound;
	
	@Before
	public void before() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new JsonOrgModule());
		
		ripeFound = mapper.readValue(loader.getResource("classpath:ripe-found.json").getInputStream(), JSONObject.class);
		ripeOther = mapper.readValue(loader.getResource("classpath:ripe-other.json").getInputStream(), JSONObject.class);
		ripeNotFound = mapper.readValue(loader.getResource("classpath:ripe-not-found.json").getInputStream(), JSONObject.class);
	}
	
	@Test
	public void ripeFound() {
		assertThat(processor.processResultDocument(ripeFound))
			.isEqualTo(new ResultDocument(new NetworkResource(new CIDR4Address(IP_ADDR_BLIZZARD_FR, 24), "FR-BLIZZARD", "FR", ENetworkRegistry.RIPE)));
	}

	@Test
	public void ripeOther() {
		assertThat(processor.processResultDocument(ripeOther)).isNull();
	}

	@Test
	public void ripeNotFound() {
		assertThat(processor.processResultDocument(ripeNotFound)).isNull();
	}
}
