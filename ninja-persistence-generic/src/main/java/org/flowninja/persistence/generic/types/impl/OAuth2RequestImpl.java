/**
 * 
 */
package org.flowninja.persistence.generic.types.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.persistence.generic.types.IOAuth2Request;

/**
 * @author rainer
 *
 */
public class OAuth2RequestImpl implements IOAuth2Request {
	private String clientId;
	private Set<String> scope;
	private Map<String, String> parameters;
	private Set<String> resourceIds;
	private Set<String> grantedAuthorities;
	private boolean approved;
	private String redirectUri;
	private Set<String> responseTypes;
	private Map<String, byte[]> extensions;
	
	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setClientId(java.lang.String)
	 */
	@Override
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getClientId()
	 */
	@Override
	public String getClientId() {
		return clientId;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setScope(java.util.Set)
	 */
	@Override
	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getScope()
	 */
	@Override
	public Set<String> getScope() {
		return scope;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setRequestParameters(java.util.Map)
	 */
	@Override
	public void setRequestParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getRequestParameters()
	 */
	@Override
	public Map<String, String> getRequestParameters() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setResourceIds(java.util.Set)
	 */
	@Override
	public void setResourceIds(Set<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getResourceIds()
	 */
	@Override
	public Set<String> getResourceIds() {
		return resourceIds;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setGrantedAuthorities(java.util.Set)
	 */
	@Override
	public void setGrantedAuthorities(Set<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getGrantedAuthorities()
	 */
	@Override
	public Set<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#isApproved()
	 */
	@Override
	public boolean isApproved() {
		return approved;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setApproved(boolean)
	 */
	@Override
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setRedirectUri(java.lang.String)
	 */
	@Override
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getRedirectUri()
	 */
	@Override
	public String getRedirectUri() {
		return redirectUri;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setResponseTypes(java.util.Set)
	 */
	@Override
	public void setResponseTypes(Set<String> responseTypes) {
		this.responseTypes = responseTypes;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getResponseTypes()
	 */
	@Override
	public Set<String> getResponseTypes() {
		return responseTypes;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#setExtensions(java.util.Map)
	 */
	@Override
	public void setExtensions(Map<String, byte[]> extensions) {
		this.extensions = extensions;
	}

	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiOAuth2Request#getExtensions()
	 */
	@Override
	public Map<String, byte[]> getExtensions() {
		return extensions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
