/**
 * 
 */
package org.flowninja.shell.admin.generic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.flowninja.persistence.generic.services.IBlockedNetworksPersistenceService;
import org.flowninja.persistence.generic.types.BlockedNetworkRecord;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.flowninja.types.net.CIDR4Address;
import org.flowninja.types.utils.CIDR4AddressRangeParser;
import org.flowninja.types.utils.NetworkAddressHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class BlockedNetworkCommands implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(BlockedNetworkCommands.class);
	
	@Autowired
	private IBlockedNetworksPersistenceService service;
	
	@CliCommand(value="blocked networks list", help="list blocked networks")
	public JSONObject listAuthorities() {
		List<JSONObject> auths = service.listBlockedNetworks().stream().map((n) -> mapToJson(n)).collect(Collectors.toList());
		
		try {
			JSONObject obj = new JSONObject();
			
			obj.put("blocked-networks", auths);
			
			return obj;
		} catch(JSONException e) {
			logger.error("listing blocked failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="blocked networks add", help="add blocked network")	
	public JSONObject addBlockedNetwork(@CliOption(help="CIDR address specification", key="cidr", mandatory=true) String cidr) {
		return mapToJson(service.addBlockedNetwork(CIDR4AddressRangeParser.parseCIDR(cidr)));
	}
	
	@CliCommand(value="blocked networks find by key", help="find blocked network by database key")
	public JSONObject findBlockedNetworkByKey(@CliOption(key="key", help="database key", mandatory=true) String key) {
		JSONObject result = null;
		
		BlockedNetworkRecord record = service.findBlockedNetwork(new BlockedNetworkKey(UUID.fromString(key)));

		if(record != null)
			result = mapToJson(record);
		
		return result;
	}
	
	@CliCommand(value="blocked networks find by address", help="find blocked network by CIDR address")
	public JSONObject findBlockedNetworkByCIDR(@CliOption(key="cidr", help="database key", mandatory=true) String cidr) {
		JSONObject result = null;
		
		BlockedNetworkRecord record = service.findBlockedNetwork(CIDR4AddressRangeParser.parseCIDR(cidr));

		if(record != null)
			result = mapToJson(record);
		
		return result;
	}
	
	@CliCommand(value="blocked networks match by host", help="match blocked network by CIDR host address")
	public JSONObject findMatchingBlockedNetworkByCIDRHost(@CliOption(key="host", help="database key", mandatory=true) String host) {
		JSONObject result = null;
		
		BlockedNetworkRecord record = service.findContainingBlockedNetwork(new CIDR4Address(NetworkAddressHelper.parseAddressSpecification(host)));

		if(record != null)
			result = mapToJson(record);
		
		return result;
	}

	@CliCommand(value="blocked networks delete", help="delete blocked network by database key")
	public void deleteBlockedNetworkByKey(@CliOption(key="key", help="database key", mandatory=true) String key) {
		service.deleteBlockedNetwork(new BlockedNetworkKey(UUID.fromString(key)));
	}	
	
	private JSONObject mapToJson(BlockedNetworkRecord address) {
		try {
			JSONObject json = new JSONObject();
	
			json.put("key", address.getKey().toString());
			json.put("cidr", address.getRange());
	
			return json;
		} catch(JSONException e) {
			logger.error("cannot map blocked network", e);
			
			throw new RuntimeException(e);
		}
	}

}
