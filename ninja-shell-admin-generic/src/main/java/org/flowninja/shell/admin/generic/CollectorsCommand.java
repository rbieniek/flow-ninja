/**
 * 
 */
package org.flowninja.shell.admin.generic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.ICollectorPersistenceService;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
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
public class CollectorsCommand implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(CollectorsCommand.class);
	
	@Autowired
	private ICollectorPersistenceService service;
	
	@CliCommand(value="collectors list", help="list collectors for an accounting group")
	public JSONObject listCollectorsForGroup(@CliOption(key="accounting-group-key", mandatory=true, help="accouting group key") String accoutingGroupKey) {
		try {
			JSONObject obj = new JSONObject();

			List<JSONObject> admins = service.listCollectorsForAccountingGroup(new AccountingGroupKey(UUID.fromString(accoutingGroupKey)))
					.stream().map((n) -> convertCollector(n)).collect(Collectors.toList());
			
			obj.put("collectors", admins);
			
			return obj;
		} catch(JSONException e) {
			logger.error("listing collectors failed", e);
			
			throw new RuntimeException(e);
		}
		
	}

	@CliCommand(value="collectors delete", help="delete collectors for an accounting group")
	public void deleteCollectorsForGroup(@CliOption(key="accounting-group-key", mandatory=true, help="accouting group key") String accoutingGroupKey) {
		try {
			service.deleteCollectorsForAccountingGroup(new AccountingGroupKey(UUID.fromString(accoutingGroupKey)));			
		} catch(JSONException e) {
			logger.error("deleting collectors failed", e);
			
			throw new RuntimeException(e);
		}
		
	}
	
	@CliCommand(value="collector find by key", help="find collector for key")
	public JSONObject findCollectorForKey(@CliOption(key="key", mandatory=true, help="collector key") String key) {
		try {
			JSONObject obj = new JSONObject();
			CollectorRecord record = service.findCollectoryByKey(new CollectorKey(UUID.fromString(key)));

			if(record != null)
				obj = convertCollector(record);

			return obj;
			
		} catch(JSONException e) {
			logger.error("find collector failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="collector find by name", help="find collector for name")
	public JSONObject findCollectorForName(@CliOption(key="name", mandatory=true, help="collector name") String name) {
		try {
			JSONObject obj = new JSONObject();
			CollectorRecord record = service.findCollectoryByName(name);

			if(record != null)
				obj = convertCollector(record);

			return obj;
			
		} catch(JSONException e) {
			logger.error("find collector failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="collector find by client-id", help="find collector for name")
	public JSONObject findCollectorForClientID(@CliOption(key="client-id", mandatory=true, help="collector client ID") String clientID) {
		try {
			JSONObject obj = new JSONObject();
			CollectorRecord record = service.findCollectoryByClientID(clientID);

			if(record != null)
				obj = convertCollector(record);

			return obj;
			
		} catch(JSONException e) {
			logger.error("find collector failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="collector create", help="create collector")	
	public JSONObject createCollector(@CliOption(key="accounting-group-key", mandatory=true, help="accouting group key") String accoutingGroupKey,
			@CliOption(key="name", mandatory=true, help="collector name") String name,
			@CliOption(key="comment", mandatory=true, help="collector comment") String comment) {
		try {
			return convertCollector(service.createCollector(new AccountingGroupKey(UUID.fromString(accoutingGroupKey)), name, comment));
		} catch(JSONException e) {
			logger.error("creating collector failed", e);
			
			throw new RuntimeException(e);
		}		
	}
	
	@CliCommand(value="collector update", help="update collector")
	public JSONObject updateCollector(@CliOption(key="key", mandatory=true, help="collector key") String key, 
			@CliOption(help="name", key="name", mandatory=false) String name, 
			@CliOption(help="comment", key="comment", mandatory=false) String comment) {
		try {
			JSONObject obj = new JSONObject();
			
			CollectorRecord record = service.findCollectoryByKey(new CollectorKey(UUID.fromString(key)));
			
			if(record != null) {
				if(StringUtils.isNotBlank(name) &&!StringUtils.equals(name, record.getName()))
					record.setName(name);

				if(StringUtils.isNotBlank(comment) &&!StringUtils.equals(comment, record.getComment()))
					record.setComment(comment);
				
				obj = convertCollector(service.updateCollector(record));
			}
			
			return obj;
		} catch(JSONException e) {
			logger.error("updating collector failed", e);
			
			throw new RuntimeException(e);
		}
	}
	
	@CliCommand(value="collector recreate-credentials", help="recreate client ID / secret pair for collector")
	public JSONObject updateCollector(@CliOption(key="key", mandatory=true, help="collector key") String key) {
		try {
			return convertCollector(service.recreateCredentials(new CollectorKey(UUID.fromString(key))));
		} catch(JSONException e) {
			logger.error("updating collector failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="collector delete", help="recreate client ID / secret pair for collector")
	public void deleteCollector(@CliOption(key="key", mandatory=true, help="collector key") String key) {
		try {
			service.deleteCollector(new CollectorKey(UUID.fromString(key)));
		} catch(JSONException e) {
			logger.error("updating collector failed", e);
			
			throw new RuntimeException(e);
		}
	}

	private JSONObject convertCollector(CollectorRecord db) {
		JSONObject json = new JSONObject();
		
		json.put("key", db.getKey().toString());
		json.put("name", db.getName());
		json.put("comment", db.getComment());
		json.put("client-id", db.getClientId());
		json.put("client-secret", db.getClientSecret());
		
		return json;
	}

}
