/**
 * 
 */
package org.flowninja.collector.client.components.impl;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.flowninja.collector.client.auth.BearerCredentials;
import org.flowninja.collector.client.components.AuthorizationClient;
import org.flowninja.collector.client.components.BearerToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

/**
 * @author rainer
 *
 */
public class AuthorizationClientImpl implements AuthorizationClient {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationClientImpl.class);
	
	private CloseableHttpClient httpClient;
	private ObjectMapper mapper;
	
	public AuthorizationClientImpl(CloseableHttpClient htttpClient) {
		this.httpClient = htttpClient;
		
		mapper = new ObjectMapper();
		mapper.registerModule(new JSR353Module());
	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.client.components.AuthorizationClient#grantClientCredentials(org.flowninja.collector.client.auth.BearerCredentials, java.lang.String)
	 */
	@Override
	public BearerToken grantClientCredentials(BearerCredentials creds, String scope) throws AuthenticationException {
		HttpHost authorizationHost = new HttpHost(creds.getAuthorizationEndpoint().getHost(), 
				creds.getAuthorizationEndpoint().getPort(), 
				creds.getAuthorizationEndpoint().getScheme());
		HttpClientContext context = HttpClientContext.create();
		List<NameValuePair> authorizationParams = new ArrayList<NameValuePair>();
		HttpPost httpPost = new HttpPost(creds.getAuthorizationEndpoint());

		logger.info("executing client credenditials flow with credentials {} and scope '{}'", creds, scope);
		
		preemptiveAuthentication(creds, authorizationHost, context);

		authorizationParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
		
		if(StringUtils.isNotBlank(scope))
			authorizationParams.add(new BasicNameValuePair("scope", scope));

		httpPost.setEntity(new UrlEncodedFormEntity(authorizationParams, Consts.UTF_8));
		httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		
		try {
			BearerToken token = httpClient.execute(authorizationHost, httpPost, new ResponseHandler<BearerToken>() {

				@Override
				public BearerToken handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					BearerToken token = null;
					
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						try {
							JsonObject payload = mapper.readValue(response.getEntity().getContent(), JsonObject.class);

							token = new BearerToken(payload.getString("access_token"), 
									Duration.ofSeconds(payload.getInt("expires_in")));
						} catch(Exception e) {
							logger.warn("failed to decode authoritzation response", e);
						}
					}
					
					return token;
				}
			}, context);

			logger.info("executing client credenditials flow with credentials {} and scope '{}' yielded bearer token {}", creds, scope, token);
			
			if(token == null)
				throw new AuthenticationException("cannot execute client credentials flow");
			
			return token;
		} catch (IOException e) {
			logger.warn("failed to execute client credentials request", e);
			
			throw new AuthenticationException("cannot process http response", e);
		}
	}

	private void preemptiveAuthentication(BearerCredentials creds, HttpHost authorizationHost, HttpClientContext context) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		
		credsProvider.setCredentials(new AuthScope(authorizationHost.getHostName(), authorizationHost.getPort()), 
				new UsernamePasswordCredentials(creds.getUserPrincipal().getName(), creds.getPassword()));
		
		authCache.put(authorizationHost, basicAuth);
		
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
	}
}
