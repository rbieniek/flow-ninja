/**
 * 
 */
package org.flowninja.persistence.mongodb.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rainer
 *
 */
@Document
public class MongoOAuth2AccessToken implements Serializable, Persistable<ObjectId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3191273768705244749L;

	@Id private ObjectId id;
	@Indexed private String value;
	private HashMap<String, Object> infos;
	private Date expiration;
	private ArrayList<String> scope;
	@DBRef private MongoOAuth2RefreshToken refreshToken;
	@DBRef private MongoOAuth2Authentication authentication;
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 */
	public void setAdditionalInformation(HashMap<String, Object> infos) {
		this.infos = infos;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getAdditionalInformation() {
		return infos;
	}
	
	/**
	 * 
	 * @param date
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getExpiration() {
		return expiration;
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
	 * @param refreshToken
	 */
	public void setRefreshToken(MongoOAuth2RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	/**
	 * 
	 * @return
	 */
	public MongoOAuth2RefreshToken getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * 
	 * @param authentication
	 */
	public void setAuthentication(MongoOAuth2Authentication authentication) {
		this.authentication = authentication;
	}
	
	/**
	 * 
	 * @return
	 */
	public MongoOAuth2Authentication getAuthentication() {
		return authentication;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean isNew() {
		return (id == null);
	}
}
