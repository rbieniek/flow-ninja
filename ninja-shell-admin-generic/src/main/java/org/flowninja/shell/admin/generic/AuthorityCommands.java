/**
 * 
 */
package org.flowninja.shell.admin.generic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.flowninja.persistence.generic.services.IAuthorityPersistenceService;
import org.flowninja.persistence.generic.types.AuthorityRecord;
import org.flowninja.types.generic.AuthorityKey;
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
public class AuthorityCommands implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(AuthorityCommands.class);
	
	@Autowired
	private IAuthorityPersistenceService service;

	@CliCommand(value="authorities list", help="list existing authorities")
	public JSONObject listAuthorities() {
		List<JSONObject> auths = service.listAuthorities().stream().map((n) -> mapToJson(n)).collect(Collectors.toList());
		
		try {
			JSONObject obj = new JSONObject();
			
			obj.put("authorities", auths);
			
			return obj;
		} catch(JSONException e) {
			logger.error("listing authorities failed", e);
			
			throw new RuntimeException(e);
		}
	}
	
	@CliCommand(value="authority create", help="create an authority")
	public JSONObject createAuthority(@CliOption(help="authority name", key="name", mandatory=true) String authority) {
		return mapToJson(service.insertAuhority(authority));
	}
	
	@CliCommand(value="authority delete", help="delete an authority")
	public void deleteAuthority(@CliOption(help="authority key", key="key", mandatory=true) String key) {
		service.deleteAuthority(new AuthorityKey(UUID.fromString(key)));
	}
	
	@CliCommand(value="authority check by name", help="check if an authority by name")
	public JSONObject checkAuthorityByName(@CliOption(help="authority name", key="name", mandatory=true) String name) {
		try {
			JSONObject json = new JSONObject();
			
			AuthorityRecord record = service.findAuthorityByAuthority(name);
			
			json.put("authority", name);
			json.put("exists", record != null);
			
			return json;
		} catch(JSONException e) {
			logger.error("cannot map authority", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="authority check by key", help="check if an authority by name")
	public JSONObject checkAuthorityByKey(@CliOption(help="authority key", key="key", mandatory=true) String key) {
		try {
			JSONObject json = new JSONObject();
			
			AuthorityRecord record = service.findAuthorityByKey(new AuthorityKey(UUID.fromString(key)));
			
			json.put("key", key);
			json.put("exists", record != null);
			
			return json;
		} catch(JSONException e) {
			logger.error("cannot map authority", e);
			
			throw new RuntimeException(e);
		}
	}

	private JSONObject mapToJson(AuthorityRecord authority) {
		try {
			JSONObject json = new JSONObject();
	
			json.put("key", authority.getKey().toString());
			json.put("authority", authority.getAuthority());
	
			return json;
		} catch(JSONException e) {
			logger.error("cannot map authority", e);
			
			throw new RuntimeException(e);
		}
	}
}
