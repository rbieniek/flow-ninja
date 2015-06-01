/**
 * 
 */
package org.flowninja.persistence.generic.types;

/**
 * @author rainer
 *
 */
public interface IOAuth2Authentication {
	/**
	 * 
	 * @param storedRequest
	 */
	public void setStoredRequest(IOAuth2Request storedRequest);
	
	/**
	 * 
	 * @return
	 */
	public IOAuth2Request getStoredRequest();
	
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
