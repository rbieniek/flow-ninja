/**
 * 
 */
package org.flowninja.rspl.client.json.arin;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.json.common.IResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
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
public class ARINResultDocumentProcessor implements IResultDocumentProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ARINResultDocumentProcessor.class);
	
	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.json.common.IResultDocumentProcessor#processResultDocument(org.json.JSONObject)
	 */
	@Override
	public NetworkResource processResultDocument(JSONObject json) {
		NetworkResource resource = null;
		
		try {
			if(json != null)
				resource = processNet(json.getJSONObject("net"));
		} catch(JSONException e) {
			logger.warn("failed to process JSON object", e);
		}
		
		logger.info("decoded JSON document to network resource: {}", resource);
		
		return resource;
	}

	private NetworkResource processNet(JSONObject json)  throws JSONException {
		NetworkResource resource = null;
		
		if(json != null) {
			String startAddress = dollarValue(json.getJSONObject("startAddress"));
			String endAddress = dollarValue(json.getJSONObject("endAddress"));
			String name = dollarValue(json.getJSONObject("name"));
			String type = processNetBlocks(json.getJSONObject("netBlocks"));
			
			if(StringUtils.isNotBlank(startAddress) && StringUtils.isNotBlank(endAddress) 
					&& StringUtils.isNotBlank(name) && (StringUtils.startsWith(type, "DA") || StringUtils.startsWith(type, "DS")))
				resource =  new NetworkResource(CIDR4AddressRangeParser.parse(String.format("%s - %s",  startAddress, endAddress)), name, null, ENetworkRegistry.ARIN);
		}
		
		return resource;
	}
	
	private String processNetBlocks(JSONObject json) throws JSONException {
		String type = null;
		
		if(json != null) {
			type = processNetBlock(json.getJSONObject("netBlock"));
		}
		
		return type;
	}

	private String processNetBlock(JSONObject json) throws JSONException {
		String type = null;
		
		if(json != null) {
			type = dollarValue(json.getJSONObject("type"));
		}
		
		return type;
	}

	private String dollarValue(JSONObject json) throws JSONException {		
		String value = null;
		
		if(json != null && json.has("$"))
			value = json.getString("$");
		
		return value;
			
	}
}
