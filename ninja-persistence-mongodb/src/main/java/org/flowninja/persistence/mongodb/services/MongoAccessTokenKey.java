/**
 * 
 */
package org.flowninja.persistence.mongodb.services;

import org.bson.types.ObjectId;
import org.flowninja.persistence.generic.types.IAccessTokenKey;


/**
 * @author rainer
 *
 */
public class MongoAccessTokenKey implements IAccessTokenKey<ObjectId> {

	private ObjectId keyValue;
	
	public MongoAccessTokenKey(ObjectId keyValue) {
		this.keyValue = keyValue;
	}
	
	/* (non-Javadoc)
	 * @see de.urb.wapi.oauth2.persistence.generic.WapiAccessTokenKey#getKeyValue()
	 */
	@Override
	public ObjectId getKeyValue() {
		return keyValue;
	}

}
