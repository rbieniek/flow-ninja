/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoOAuth2Authentication implements Serializable, Persistable<ObjectId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8825463132520470428L;

	@Id private ObjectId id;
	@Indexed private String clientId;
	private ArrayList<String> scope;
	private HashMap<String, String> parameters;
	private ArrayList<String> resourceIds;
	private ArrayList<String> grantedAuthorities;
	private boolean approved;
	private String redirectUri;
	private ArrayList<String> responseTypes;
	private HashMap<String, Binary> extensions;
	private Binary userAuthentication;

	/**
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * 
	 * @param scope
	 */
	public void setScope(ArrayList<String> scope) {
		this.scope = scope;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getScope() {
		return scope;
	}
	
	/**
	 * 
	 * @param parameters
	 */
	public void setRequestParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, String> getRequestParameters() {
		return parameters;
	}
	
	/**
	 * 
	 * @param resourceIds
	 */
	public void setResourceIds(ArrayList<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getResourceIds() {
		return resourceIds;
	}
	
	/**
	 * 
	 * @param grantedAuthorities
	 */
	public void setGrantedAuthorities(ArrayList<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isApproved() {
		return approved;
	}
	
	/**
	 * 
	 * @param approved
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	/**
	 * 
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRedirectUri() {
		return redirectUri;
	}
	
	/**
	 * 
	 * @param responseTypes
	 */
	public void setResponseTypes(ArrayList<String> responseTypes) {
		this.responseTypes = responseTypes;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getResponseTypes() {
		return responseTypes;
	}
	
	/**
	 * 
	 * @param extensions
	 */
	public void setExtensions(HashMap<String, Binary> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Binary> getExtensions() {
		return extensions;
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the userAuthentication
	 */
	public Binary getUserAuthentication() {
		return userAuthentication;
	}

	/**
	 * @param userAuthentication the userAuthentication to set
	 */
	public void setUserAuthentication(Binary userAuthentication) {
		this.userAuthentication = userAuthentication;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean isNew() {
		return (id == null);
	}
}
