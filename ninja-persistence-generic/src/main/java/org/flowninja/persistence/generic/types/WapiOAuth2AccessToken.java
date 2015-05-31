/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author rainer
 *
 */
public interface WapiOAuth2AccessToken {
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value);
	
	/**
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * 
	 */
	public void setAdditionalInformation(Map<String, Object> infos);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getAdditionalInformation();
	
	/**
	 * 
	 * @param date
	 */
	public void setExpiration(Date date);
	
	/**
	 * 
	 * @return
	 */
	public Date getExpiration();

	/**
	 * 
	 * @param scope
	 */
	public void setScope(Set<String> scope);
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getScope();
	
	/**
	 * 
	 * @param refreshToken
	 */
	public void setRefreshToken(WapiOAuth2RefreshToken refreshToken);
	
	/**
	 * 
	 * @return
	 */
	public WapiOAuth2RefreshToken getRefreshToken();
	
	/**
	 * 
	 * @param authentication
	 */
	public void setAuthentication(WapiOAuth2Authentication authentication);
	
	/**
	 * 
	 * @return
	 */
	public WapiOAuth2Authentication getAuthentication();
}
