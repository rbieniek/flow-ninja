/**
 * 
 */
package org.flowninja.persistence.generic.types;

/**
 * @author rainer
 *
 */
public interface WapiOAuth2Authentication {
	/**
	 * 
	 * @param storedRequest
	 */
	public void setStoredRequest(WapiOAuth2Request storedRequest);
	
	/**
	 * 
	 * @return
	 */
	public WapiOAuth2Request getStoredRequest();
	
	/**
	 * 
	 * @param authentication
	 */
	public void setAuthentication(byte[] authentication);
	
	/**
	 * 
	 * @return
	 */
	public byte[] getAuthentication();
}
