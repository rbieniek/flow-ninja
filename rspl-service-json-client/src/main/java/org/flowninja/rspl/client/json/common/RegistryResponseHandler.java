/**
 * 
 */
package org.flowninja.rspl.client.json.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

/**
 * @author rainer
 *
 */
@Component
public class RegistryResponseHandler implements ResponseHandler<JSONObject> {
	private static final Logger logger = LoggerFactory.getLogger(RegistryResponseHandler.class);

	private JsonFactory factory;
	private ObjectMapper mapper;
	
	public RegistryResponseHandler() {
		factory = new JsonFactory();
		mapper = new ObjectMapper(factory);
		
		mapper.registerModule(new JsonOrgModule());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		JSONObject json = null;		
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		logger.info("processing result with status line '{}'", statusLine);
		
		if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
			ContentType contentType = ContentType.getOrDefault(entity);
			Charset charset = contentType.getCharset();

			logger.info("server returned Content-Type: {}", contentType);
			
			if(charset == null)
				charset = Charset.forName("UTF-8");
			
			try {
				json = mapper.readValue(new InputStreamReader(entity.getContent(), charset), JSONObject.class);
			} catch(JsonParseException e) {
				logger.warn("cannot process result JSON document", e);
			} catch(JsonMappingException e) {
				logger.warn("cannot process result JSON document", e);
			}
		}
		
		return json;
	}

}
