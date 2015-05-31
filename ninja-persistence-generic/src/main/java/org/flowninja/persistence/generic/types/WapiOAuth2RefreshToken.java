/**
 * 
 */
package org.flowninja.persistence.generic.types;

import java.util.Date;

/**
 * @author rainer
 *
 */
public interface WapiOAuth2RefreshToken {
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
	 * @param expiration
	 */
	public void setExpiration(Date expiration);
	
	/**
	 * 
	 * @return
	 */
	public Date getExpiration();
}
