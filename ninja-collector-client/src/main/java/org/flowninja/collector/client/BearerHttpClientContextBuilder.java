/**
 * 
 */
package org.flowninja.collector.client;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.flowninja.collector.client.auth.BearerCredentials;
import org.flowninja.collector.client.auth.BearerSchemeFactory;

/**
 * @author rainer
 *
 */
public class BearerHttpClientContextBuilder {

	private HttpClientContext context;
	private CredentialsProvider credentialsProvider;
	private RequestConfig.Builder configBuilder;
	private AuthCache authCache;
	private BearerSchemeFactory schemeFactory;
	
	private BearerHttpClientContextBuilder(HttpClientContext context) {
		if(context == null)
			this.context = HttpClientContext.create();
		else
			this.context = context;
		
		credentialsProvider = new BasicCredentialsProvider();
		this.configBuilder = RequestConfig.custom();
		this.authCache = new BasicAuthCache();
	}
	
	public static BearerHttpClientContextBuilder create() {
		return new BearerHttpClientContextBuilder(null);
	}
	
	public static BearerHttpClientContextBuilder create(HttpClientContext context) {
		return new BearerHttpClientContextBuilder(context);
	}

	public BearerHttpClientContextBuilder withCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
		
		return this;
	}

	public BearerHttpClientContextBuilder withBearerCredentials(AuthScope scope, BearerCredentials credentials) {
		this.credentialsProvider.setCredentials(scope, credentials);
		
		return this;
	}
	
	public BearerHttpClientContextBuilder withRequestConfigBuilder(RequestConfig.Builder builder) {
		this.configBuilder = builder;
		
		return this;
	}
	
	public BearerHttpClientContextBuilder withAuthCache(AuthCache authCache) {
		this.authCache = authCache;
		
		return this;
	}
	
	public BearerHttpClientContextBuilder withBearerSchemeFactory(BearerSchemeFactory schemeFactory) {
		this.schemeFactory = schemeFactory;
		
		return this;
	}
	
	public HttpClientContext build() {
		configBuilder.setAuthenticationEnabled(true).setTargetPreferredAuthSchemes(BearerSchemeFactory.SCHEME_COLLECTION);
		
		context.setCredentialsProvider(credentialsProvider);
		context.setRequestConfig(configBuilder.build());
		context.setAuthCache(authCache);
		context.setAuthSchemeRegistry(RegistryBuilder.<AuthSchemeProvider>create().register(BearerSchemeFactory.SCHEME, schemeFactory).build());
		
		return context;
	}
}
