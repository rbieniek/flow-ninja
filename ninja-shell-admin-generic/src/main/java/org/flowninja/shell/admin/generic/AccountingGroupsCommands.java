/**
 * 
 */
package org.flowninja.shell.admin.generic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.persistence.generic.services.IAccountingGroupPersistenceService;
import org.flowninja.persistence.generic.services.ICollectorPersistenceService;
import org.flowninja.persistence.generic.types.AccountingGroupRecord;
import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.types.generic.AccountingGroupKey;
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
public class AccountingGroupsCommands implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(AccountingGroupsCommands.class);
	
	@Autowired
	private IAccountingGroupPersistenceService groupService;
	
	@Autowired
	private ICollectorPersistenceService collectorService;
	
	@CliCommand(value="accounting groups list", help="list existing accounting groups")
	public JSONObject listAccoutingGroups(@CliOption(key="deep", help="display all dependent objects", mandatory=false, unspecifiedDefaultValue="false") Boolean deep) {
		try {
			JSONObject obj = new JSONObject();

			List<JSONObject> admins = groupService.listAccountingGroups().stream().map((n) -> convertAccountingGroup(n, deep)).collect(Collectors.toList());
			
			obj.put("accounting-groups", admins);
			
			return obj;
		} catch(JSONException e) {
			logger.error("listing accounting groups failed", e);
			
			throw new RuntimeException(e);
		}
	}
	
	@CliCommand(value="accounting group find by key", help="find accouting group by key")
	public JSONObject findAccountingGroupByKey(@CliOption(help="accounting group key", key="key", mandatory=true) String key, 
			@CliOption(key="deep", help="display all dependent objects", unspecifiedDefaultValue="false") Boolean deep) {
		try {
			JSONObject obj = new JSONObject();
			AccountingGroupRecord group = groupService.findByKey(new AccountingGroupKey(UUID.fromString(key)));

			if(group != null)
				obj = convertAccountingGroup(group, deep);
			
			return obj;
		} catch(JSONException e) {
			logger.error("finding accounting group failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="accounting group find by name", help="find accouting group by name")
	public JSONObject findAccountingGroupByName(@CliOption(help="accounting group name", key="name", mandatory=true) String name, 
			@CliOption(key="deep", help="display aall dependent objects", unspecifiedDefaultValue="false") Boolean deep) {
		try {
			JSONObject obj = new JSONObject();
			AccountingGroupRecord group = groupService.findByName(name);

			if(group != null)
				obj = convertAccountingGroup(group, deep);
			
			return obj;
		} catch(JSONException e) {
			logger.error("finding accounting group failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="accounting group create", help="update accounting group")
	public JSONObject createAccountingGroup(@CliOption(help="name", key="name", mandatory=true) String name, 
			@CliOption(help="comment", key="comment", mandatory=true) String comment) {
		try {
			AccountingGroupRecord group = groupService.createAccountingGroup(name, comment);
			
			return convertAccountingGroup(group, false);
		} catch(JSONException e) {
			logger.error("finding accounting group failed", e);
			
			throw new RuntimeException(e);
		}
		
	}

	@CliCommand(value="accounting group update", help="update accounting group")
	public JSONObject updateAccountingGroup(@CliOption(help="accounting group key", key="key", mandatory=true) String key, 
			@CliOption(help="name", key="name", mandatory=false) String name, 
			@CliOption(help="comment", key="comment", mandatory=false) String comment) {
		try {
			JSONObject obj = new JSONObject();
			AccountingGroupRecord group = groupService.findByKey(new AccountingGroupKey(UUID.fromString(key)));

			if(group != null) {
				if(!StringUtils.equals(name, group.getName()))
					group.setName(name);
				if(!StringUtils.equals(comment, group.getComment()))
					group.setComment(comment);
				
				obj = convertAccountingGroup(groupService.updateAccoutingGroup(group), false);
			}
			
			return obj;
		} catch(JSONException e) {
			logger.error("finding accounting group failed", e);
			
			throw new RuntimeException(e);
		}
		
	}

	@CliCommand(value="accounting group delete", help="update accounting group")
	public void deleteAccoutingGroup(@CliOption(help="accounting group key", key="key", mandatory=true) String key) {
		AccountingGroupRecord group = groupService.findByKey(new AccountingGroupKey(UUID.fromString(key)));
		
		if(group != null) {
			collectorService.deleteCollectorsForAccountingGroup(group.getKey());
			groupService.deleteAccountingGroup(group.getKey());
		}
	}

	private JSONObject convertAccountingGroup(AccountingGroupRecord db, boolean deep) {
		JSONObject json = new JSONObject();
		
		json.put("key", db.getKey());
		json.put("name", db.getName());
		json.put("comment", db.getComment());
//		json.put("created-when", db.getCreatedWhen());
//		json.put("last-modified", db.getLastModifiedAt());
		
		if(deep) {
			List<JSONObject> collectors = collectorService.listCollectorsForAccountingGroup(db.getKey()).stream().map((n) -> convertCollector(n)).collect(Collectors.toList());
			
			json.put("collectors", collectors);
		}
		
		return json;
	}

	private JSONObject convertCollector(CollectorRecord db) {
		JSONObject json = new JSONObject();
		
		json.put("key", db.getKey());
		json.put("name", db.getName());
		json.put("comment", db.getComment());
		json.put("client-id", db.getClientId());
		json.put("client-secret", db.getClientSecret());
//		json.put("created-when", db.getCreatedWhen());
//		json.put("last-modified", db.getLastModifiedAt());
		
		return json;
	}
}
