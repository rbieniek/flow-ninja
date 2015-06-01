/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.util.Map;
import java.util.Set;

/**
 * @author rainer
 *
 */
public interface IOAuth2Request {
	/**
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId);
	
	/**
	 * 
	 * @return
	 */
	public String getClientId();
	
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
	 * @param parameters
	 */
	public void setRequestParameters(Map<String, String> parameters);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getRequestParameters();
	
	/**
	 * 
	 * @param resourceIds
	 */
	public void setResourceIds(Set<String> resourceIds);
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getResourceIds();
	
	/**
	 * 
	 * @param grantedAuthorities
	 */
	public void setGrantedAuthorities(Set<String> grantedAuthorities);
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getGrantedAuthorities();
	
	/**
	 * 
	 * @return
	 */
	public boolean isApproved();
	
	/**
	 * 
	 * @param approved
	 */
	public void setApproved(boolean approved);
	
	/**
	 * 
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri);
	
	/**
	 * 
	 * @return
	 */
	public String getRedirectUri();
	
	/**
	 * 
	 * @param responseTypes
	 */
	public void setResponseTypes(Set<String> responseTypes);
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getResponseTypes();
	
	/**
	 * 
	 * @param extensions
	 */
	public void setExtensions(Map<String, byte[]> extensions);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, byte[]> getExtensions();
}
