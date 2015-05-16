/**
 * 
 */
package org.flowninja.rspl.client.whois.common;

import static org.fest.assertions.api.Assertions.*;

import java.io.InputStream;

import org.flowninja.rspl.client.whois.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class WhoisStreamParserTest {

	@Autowired
	private ResourceLoader loader;

	@Autowired
	private WhoisStreamParser parser;
	
	private InputStream afriNic;
	private InputStream apNic;
	private InputStream lacNic;
	
	@Before
	public void before() throws Exception {
		afriNic = loader.getResource("classpath:www-afrinic-net.txt").getInputStream();
		apNic = loader.getResource("classpath:www-apnic-net.txt").getInputStream();
		lacNic = loader.getResource("classpath:www-lacnic-net.txt").getInputStream();
	}
	
	@After
	public void after() throws Exception {
		afriNic.close();
		apNic.close();
		lacNic.close();
	}
	
	@Test
	public void parseAfriNic() {
		assertThat(parser.parseWhoisResponse(afriNic))
			.containsOnly(new InetnumRecord("196.216.2.0 - 196.216.3.255", 
				"AFRINIC", 
				new String[] {"AfriNIC - Internal Use"},
				"CA15-AFRINIC",
				"IT7-AFRINIC",
				"AFRINIC # Filtered",
				"ZA",
				"ORG-AFNC1-AFRINIC",
				null));
	}
	
	@Test
	public void parseApNic() {
		assertThat(parser.parseWhoisResponse(apNic))
			.containsOnly(new InetnumRecord("203.119.96.0 - 203.119.111.255", 
				"APNIC-SERVICES", 
				new String[] {
					"Asia Pacific Network Information Centre",
					"Regional Internet Registry for the Asia-Pacific Region",
					"6 Cordelia Street",
					"PO Box 3646",
					"South Brisbane, QLD 4101"
				},
				"AIC1-AP",
				"AIC1-AP",
				"APNIC",
				"AU",
				null,
				null));
	}

	
	@Test
	public void parseLacNic() {
		assertThat(parser.parseWhoisResponse(lacNic))
			.containsOnly(new InetnumRecord("200.3.12/22", 
				null, 
				null,
				null,
				"AIL",
				null,
				"UY",
				null,
				"LACNIC - Latin American and Caribbean IP address"));
	}
}
