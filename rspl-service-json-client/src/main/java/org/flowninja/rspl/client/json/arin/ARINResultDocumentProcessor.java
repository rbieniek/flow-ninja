/**
 * 
 */
package org.flowninja.rspl.client.json.arin;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.rspl.client.json.common.IResultDocumentProcessor;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.rspl.definitions.types.ResultDocument;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
import org.flowninja.types.utils.NetworkAddressHelper;
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
public class ARINResultDocumentProcessor implements IResultDocumentProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ARINResultDocumentProcessor.class);
	
	/* (non-Javadoc)
	 * @see org.flowninja.rspl.client.json.common.IResultDocumentProcessor#processResultDocument(org.json.JSONObject)
	 */
	@Override
	public ResultDocument processResultDocument(byte[] networkAddress, JSONObject json) {
		ResultDocument resource = null;
		
		try {
			if(json != null)
				resource = processNet(networkAddress, json.getJSONObject("net"));
		} catch(JSONException e) {
			logger.warn("failed to process JSON object", e);
		}
		
		logger.info("decoded JSON document to network resource: {}", resource);
		
		return resource;
	}

	private ResultDocument processNet(byte[] networkAddress,JSONObject json)  throws JSONException {
		ResultDocument resource = null;
		
		if(json != null) {
			String startAddress = dollarValue(json.getJSONObject("startAddress"));
			String endAddress = dollarValue(json.getJSONObject("endAddress"));
			String name = dollarValue(json.getJSONObject("name"));
			String type = processNetBlocks(networkAddress, json.getJSONObject("netBlocks"));
			
			if(StringUtils.isNotBlank(startAddress) && StringUtils.isNotBlank(endAddress) && StringUtils.isNotBlank(name)) {
				if(StringUtils.equals(type, "DA") || StringUtils.equals(type, "DS") || StringUtils.equals(type, "S"))
					resource =  new ResultDocument(new NetworkResource(CIDR4AddressRangeParser.parse(String.format("%s - %s",  startAddress, endAddress)), name, 
							null, ENetworkRegistry.ARIN));
				else if(StringUtils.equals(type, "RX")) {
					String org = json.getJSONObject("orgRef").getString("@handle");
					
					if(StringUtils.equals(org, "RIPE"))
						resource = new ResultDocument(ENetworkRegistry.RIPE);
				}
			}
		}
		
		return resource;
	}
	
	
	private String processNetBlocks(byte[] networkAddress, JSONObject json) throws JSONException {
		String type = null;
		
		if(json != null) {
			JSONArray array = json.optJSONArray("netBlock");
			JSONObject obj = json.optJSONObject("netBlock");
			
			if(obj != null) {
				type = processNetBlock(json.getJSONObject("netBlock"));
			} else if(array != null){
				for(int i=0; i<array.length(); i++) {
					if((type = processNetBlock(networkAddress, array.getJSONObject(i))) != null)
						break;
				}
			}
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

	private String processNetBlock(byte[] networkAddress, JSONObject json) throws JSONException {
		String type = null;
		String startAddress = dollarValue(json.getJSONObject("startAddress"));
		String cidrLength = dollarValue(json.getJSONObject("cidrLength"));
		
		try {
			CIDR4Address addr = new CIDR4Address(NetworkAddressHelper.parseAddressSpecification(startAddress), Integer.parseInt(cidrLength));
			
			if(addr.isInRange(networkAddress))
				type = dollarValue(json.getJSONObject("type"));
		} catch(Exception e) {
			throw new JSONException(e);
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
