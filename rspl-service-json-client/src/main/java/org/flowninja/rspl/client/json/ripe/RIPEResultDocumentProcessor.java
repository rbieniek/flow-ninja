/**
 * 
 */
package org.flowninja.rspl.client.json.ripe;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.json.common.IResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class RIPEResultDocumentProcessor implements IResultDocumentProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RIPEResultDocumentProcessor.class);

	@Override
	public NetworkResource processResultDocument(JSONObject json) {
		NetworkResource resource = null;
				
		try {
			if(json != null)
				resource = processObjects(json.getJSONObject("objects"));
		} catch(JSONException e) {
			logger.warn("failed to process JSON object", e);
		}
		
		logger.info("decoded JSON document to network resource: {}", resource);
		
		return resource;
	}
	
	private NetworkResource processObjects(JSONObject json) throws JSONException {
		NetworkResource resource = null;

		if(json != null) {
			resource = processObject(json.getJSONArray("object"));
		}
		
		return resource;
		
	}

	private NetworkResource processObject(JSONArray array) throws JSONException {
		NetworkResource resource = null;

		if(array != null) {
			for(int i=0; i<array.length(); i++) {
				if((resource = processEntry(array.getJSONObject(i))) != null)
					break;
			}
		}
		
		return resource;
		
	}
	
	private NetworkResource processEntry(JSONObject json) throws JSONException {
		NetworkResource resource = null;
		
		if(StringUtils.equals(json.getString("type"), "inetnum") 
				&& json.getJSONObject("source") != null && StringUtils.equals(json.getJSONObject("source").getString("id"), "ripe-grs")) {
			JSONArray attributes = null;
			
			if(json.getJSONObject("attributes") != null && (attributes = json.getJSONObject("attributes").getJSONArray("attribute")) != null) {
				CIDR4Address range = null;
				String netName = null;
				String country = null;

				for(int i=0; i<attributes.length(); i++) {
					JSONObject attr = attributes.getJSONObject(i);
					
					String name = attr.getString("name");
					String value = attr.getString("value");
					
					if(StringUtils.isNotBlank(value)) {
						if(StringUtils.equals(name, "inetnum")) {
							range = CIDR4AddressRangeParser.parse(value);
						} else if(StringUtils.equals(name, "netname")) {
							netName = value;
						} else if(StringUtils.equals(name, "country")) {
							country = value;
						}
					}
				}
				
				if(range != null && netName != null)
					resource = new NetworkResource(range, netName, country, ENetworkRegistry.RIPE);
			}
		}
		
		return resource;
	}
}
