/**
 * 
 */
package org.flowninja.rspl.client.rdap.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

/**
 * @author rainer
 *
 */
@Component
public class RdapPayloadResponseHandler implements ResponseHandler<NetworkResource> {
	private static final Logger logger = LoggerFactory.getLogger(RdapPayloadResponseHandler.class);
	
	private JsonFactory factory;
	private ObjectMapper mapper;

	public RdapPayloadResponseHandler() {
		factory = new JsonFactory();
		mapper = new ObjectMapper(factory);
		
		mapper.registerModule(new JsonOrgModule());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	@Override
	public NetworkResource handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		NetworkResource resource = null;

		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		logger.info("processing result with status line '{}'", statusLine);
		
		if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
			ContentType contentType = ContentType.getOrDefault(entity);
			Charset charset = contentType.getCharset();

			logger.info("server returned Content-Type: {}", contentType);
			
			if(charset == null)
				charset = Charset.forName("UTF-8");
			
			try {
				JSONObject root = mapper.readValue(new InputStreamReader(entity.getContent(), charset), JSONObject.class);
				
				String startAddress = root.optString("startAddress");
				String endAddress = root.optString("endAddress");
				String name = root.optString("name");
				String country = root.optString("country");
				String port43 = root.optString("port43");
				
				if(StringUtils.isNotBlank(startAddress) && StringUtils.isNotBlank(endAddress) && StringUtils.isNotBlank(name)) {
					ENetworkRegistry registry = null;
					
					if(StringUtils.contains(port43, "apnic"))
						registry = ENetworkRegistry.APNIC;
					else if(StringUtils.contains(port43, "lacnic"))
						registry = ENetworkRegistry.LACNIC;
					
					resource = new NetworkResource(CIDR4AddressRangeParser.parse(String.format("%s-%s", startAddress, endAddress)), 
						name, country, registry);
				}
			} catch(JsonParseException e) {
				logger.warn("cannot process result JSON document", e);
			} catch(JsonMappingException e) {
				logger.warn("cannot process result JSON document", e);
			} catch(Exception e) {
				logger.warn("cannot process result", e);				
			}
		}

		return resource;
	}

}
