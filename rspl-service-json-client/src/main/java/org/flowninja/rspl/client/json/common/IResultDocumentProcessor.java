/**
 * 
 */
package org.flowninja.rspl.client.json.common;

import org.flowninja.rspl.definitions.types.NetworkResource;
import org.json.JSONObject;

/**
 * Process a RESTful service result document and convert into a {@link ResolveResult} instance 
 * 
 * @author rainer
 * @see ResolveResult
 *
 */
public interface IResultDocumentProcessor {

	/**
	 * Process the service result document
	 * 
	 * @param json the JSON document to process. Maybe <code>null</code>
	 * @return the result of the resolve process. Will be <code>null</code> if the passed JSON document was <code>null</code>
	 */
	NetworkResource processResultDocument(JSONObject json);
}
