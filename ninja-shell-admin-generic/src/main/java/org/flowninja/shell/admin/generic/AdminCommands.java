/**
 * 
 */
package org.flowninja.shell.admin.generic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.flowninja.persistence.generic.services.IAdminPersistenceService;
import org.flowninja.persistence.generic.services.IAuthorityPersistenceService;
import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.generic.types.AdminRecord;
import org.flowninja.persistence.generic.types.AuthorityKey;
import org.flowninja.persistence.generic.types.AuthorityRecord;
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
public class AdminCommands implements CommandMarker {
	private static final Logger logger = LoggerFactory.getLogger(AdminCommands.class);
	
	private static final String MAIL_REGEX = "^[A-Z0-9][A-Z0-9._%+-]*@[A-Z0-9]+[A-Z0-9.-]*\\.[A-Z]{2,4}$";
	
	@Autowired
	private IAdminPersistenceService adminService;
	
	@Autowired
	private IAuthorityPersistenceService authorityService;
	
	private Pattern mailAddrPattern;
	
	public AdminCommands() {
		mailAddrPattern = Pattern.compile(MAIL_REGEX, Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
	}
	
	@CliCommand(value="administrators list", help="list existing administrators")
	public JSONObject listAdmins() {
		try {
			JSONObject obj = new JSONObject();
			List<JSONObject> admins = adminService.listAdmins().stream().map((n) -> convertAdmin(n)).collect(Collectors.toList());
			
			obj.put("administrators", admins);
			
			return obj;
		} catch(JSONException e) {
			logger.error("listing administrators failed", e);
			
			throw new RuntimeException(e);
		}
	}

	@CliCommand(value="administrator create", help="create administrator account")
	public JSONObject createAdmin(@CliOption(key="user-name", mandatory=true, help="administrator mail address") String userName, 
			@CliOption(key="password", mandatory=true, help="administrator password") String password,
			@CliOption(key="authority-names", mandatory=false, help="comma-seperated list of assigned authority names") String authorityNames,
			@CliOption(key="authority-keys", mandatory=false, help="comma-seperated list of assigned authority keys") String authorityKeys) {
		if(!mailAddrPattern.matcher(userName).matches()) {
			throw new IllegalArgumentException("Illegal username passed: " + userName);
		}
		if(StringUtils.isBlank(password))
			throw new IllegalArgumentException("Blank password disallowed");
				
		return convertAdmin(adminService.createAdmin(userName, password, parseAuthorityArguments(authorityNames, authorityKeys)));
	}
	
	@CliCommand(value="administrator assign authorities", help="assign authorities to administrator account")
	public JSONObject assignAdminAuthorities(@CliOption(key="administrator-key", mandatory=true, help="administrator key") String adminKey,
			@CliOption(key="authority-names", mandatory=false, help="comma-seperated list of assigned authority names") String authorityNames,
			@CliOption(key="authority-keys", mandatory=false, help="comma-seperated list of assigned authority keys") String authorityKeys) {
		AdminRecord admin = adminService.findByKey(new AdminKey(UUID.fromString(adminKey)));
		
		if(admin == null)
			throw new IllegalArgumentException("Unknown admin key passed: " + adminKey);
		
		return convertAdmin(adminService.assignAuthorities(admin.getKey(), parseAuthorityArguments(authorityNames, authorityKeys)));
	}

	@CliCommand(value="administrator assign password", help="assign password to administrator account")
	public JSONObject assignAdminPassword(@CliOption(key="administrator-key", mandatory=true, help="administrator key") String adminKey,
			@CliOption(key="password", mandatory=true, help="administrator password") String password) {
		AdminRecord admin = adminService.findByKey(new AdminKey(UUID.fromString(adminKey)));
		
		if(admin == null)
			throw new IllegalArgumentException("Unknown admin key passed: " + adminKey);

		if(StringUtils.isBlank(password))
			throw new IllegalArgumentException("Blank password disallowed");

		return convertAdmin(adminService.assignPassword(admin.getKey(), password));
	}

	@CliCommand(value="administrator delete", help="delete administrator account")
	public void deleteAdmin(@CliOption(key="administrator-key", mandatory=true, help="administrator key") String adminKey) {
		AdminRecord admin = adminService.findByKey(new AdminKey(UUID.fromString(adminKey)));
		
		if(admin == null)
			throw new IllegalArgumentException("Unknown admin key passed: " + adminKey);

		adminService.deleteAdmin(admin.getKey());
	}
	
	private Set<AuthorityKey> parseAuthorityArguments(String authorityNames, String authorityKeys) {
		Set<AuthorityKey> authKeys = new HashSet<AuthorityKey>();

		if(StringUtils.isNotBlank(authorityNames))
			(new StrTokenizer(authorityNames, ",")).getTokenList().forEach((n) -> {
				AuthorityRecord auth = authorityService.findAuthorityByAuthority(n);
				
				if(auth != null)
					authKeys.add(auth.getKey());
				else 
					throw new IllegalArgumentException("Unknown authority given: " + n);
			});
		if(StringUtils.isNotBlank(authorityKeys))
			(new StrTokenizer(authorityKeys, ",")).getTokenList().forEach((n) -> {
				AuthorityRecord auth = authorityService.findAuthorityByKey(new AuthorityKey(UUID.fromString(n)));
				
				if(auth != null)
					authKeys.add(auth.getKey());
				else 
					throw new IllegalArgumentException("Unknown authority key given: " + n);				
			});
		
		return authKeys;
	}
	
	private JSONObject convertAdmin(AdminRecord admin) {
		try {
			JSONObject adminObj = new JSONObject();
			List<JSONObject> authorities = admin.getAuthorities().stream().map((au) -> {
				try {
					JSONObject authObject = new JSONObject();
					
					authObject.put("authority", au.getAuthority());
					authObject.put("key", au.getKey().toString());

					return authObject;
				} catch(JSONException e) {
					logger.error("listing administrators failed", e);
					
					throw new RuntimeException(e);
				}
			}).collect(Collectors.toList());
			
			adminObj.put("userName", admin.getUserName());
			adminObj.put("key", admin.getKey().toString());
			adminObj.put("authorities", authorities);
			
			return adminObj;
		} catch(JSONException e) {					
			logger.error("listing administrators failed", e);
			
			throw new RuntimeException(e);
		}
		
	}
}
