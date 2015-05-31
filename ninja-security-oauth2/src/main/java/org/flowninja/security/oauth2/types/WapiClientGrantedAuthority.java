/**
 * 
 */
package org.flowninja.security.oauth2.types;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author rainer
 *
 */
public class WapiClientGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6409999647192242170L;

	private String authority;
	
	public WapiClientGrantedAuthority() {}
	
	public WapiClientGrantedAuthority(String authority) {
		this.authority = authority;
	}
 	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		return authority;
	}

}
