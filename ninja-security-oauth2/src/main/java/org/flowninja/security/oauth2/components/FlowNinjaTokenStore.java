/**
 * 
 */
package org.flowninja.security.oauth2.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.flowninja.persistence.generic.services.IOAuth2TokenPersistenceService;
import org.flowninja.persistence.generic.types.IOAuth2AccessToken;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.IOAuth2RefreshToken;
import org.flowninja.persistence.generic.types.IOAuth2Request;
import org.flowninja.persistence.generic.types.impl.OAuth2AccessTokenImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2AuthenticationImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RefreshTokenImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RequestImpl;
import org.flowninja.security.oauth2.types.FlowNinjaClientGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author rainer
 *
 */
public class FlowNinjaTokenStore implements TokenStore {
	private static final Logger logger = LoggerFactory.getLogger(FlowNinjaTokenStore.class);

	@Autowired
	private IOAuth2TokenPersistenceService wapiTokenStore;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#readAuthentication(org.springframework.security.oauth2.common.OAuth2AccessToken)
	 */
	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		OAuth2Authentication oa = null;
		
		logger.info("reading authentication for token {}", token);
		
		IOAuth2Authentication auth = wapiTokenStore.readAuthenticationForAccessToken(token.getValue());

		if(auth != null) {
			try {
				oa = new OAuth2Authentication(restoreAuthentication(auth.getStoredRequest()), deserializeObject(auth.getAuthentication()));
			} catch(RuntimeException e) {
				throw e;
			} catch(Exception e) {
				logger.error("failed to deserilize object", e);
				throw new RuntimeException(e);
			}
		}
		
		return oa;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#readAuthentication(java.lang.String)
	 */
	@Override
	public OAuth2Authentication readAuthentication(String token) {
		OAuth2Authentication oa = null;

		logger.info("reading authentication for token value {}", token);

		IOAuth2Authentication auth = wapiTokenStore.readAuthenticationForAccessToken(token);

		if(auth != null) {
			try {
				oa = new OAuth2Authentication(restoreAuthentication(auth.getStoredRequest()), deserializeObject(auth.getAuthentication()));
			} catch(RuntimeException e) {
				throw e;
			} catch(Exception e) {
				logger.error("failed to deserilize object", e);
				throw new RuntimeException(e);
			}
		}
		
		return oa;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#storeAccessToken(org.springframework.security.oauth2.common.OAuth2AccessToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		logger.info("storing access token for token {} and authentication {}", token, authentication);

		IOAuth2AccessToken wapiToken = new OAuth2AccessTokenImpl();
		
		wapiToken.setAdditionalInformation(token.getAdditionalInformation());
		wapiToken.setExpiration(token.getExpiration());
		wapiToken.setScope(token.getScope());
		wapiToken.setValue(token.getValue());
		
		if(token.getRefreshToken() != null) {
			OAuth2RefreshToken refreshToken = token.getRefreshToken();
			IOAuth2RefreshToken wapiRefreshToken = new OAuth2RefreshTokenImpl();
			
			wapiRefreshToken.setValue(refreshToken.getValue());
			
			if(refreshToken instanceof ExpiringOAuth2RefreshToken)
				wapiRefreshToken.setExpiration(((ExpiringOAuth2RefreshToken)refreshToken).getExpiration());
			
			wapiToken.setRefreshToken(wapiRefreshToken);
		}

		IOAuth2Authentication wapiAuthentication = new OAuth2AuthenticationImpl();
		IOAuth2Request wapiRequest = new OAuth2RequestImpl();		
		OAuth2Request request = authentication.getOAuth2Request();
		
		wapiRequest.setApproved(request.isApproved());
		wapiRequest.setClientId(request.getClientId());
		
		if(request.getExtensions() != null) {
			Map<String, byte[]> extensions = new HashMap<String, byte[]>();

			request.getExtensions().forEach((k, v) -> { 
				try {
					extensions.put(k, serializeObject(v)); 
				} catch(IOException e) {
					logger.error("Cannot serialize object", e);
						
					throw new RuntimeException(e);
				}			
			});
			
			wapiRequest.setExtensions(extensions);

		}
		
		wapiRequest.setGrantedAuthorities(request.getAuthorities().stream().map((n) -> n.getAuthority()).collect(Collectors.toSet()));
		wapiRequest.setRedirectUri(request.getRedirectUri());
		wapiRequest.setRequestParameters(request.getRequestParameters());
		wapiRequest.setResourceIds(request.getResourceIds());
		wapiRequest.setResponseTypes(request.getResponseTypes());
		wapiRequest.setScope(request.getScope());
		
		if(authentication.getUserAuthentication() != null) {
			try {
				wapiAuthentication.setAuthentication(serializeObject(authentication.getUserAuthentication()));
			} catch(IOException e) {
				logger.error("Cannot serialize object", e);
				
				throw new RuntimeException(e);				
			}			
		}
		wapiAuthentication.setStoredRequest(wapiRequest);
		wapiToken.setAuthentication(wapiAuthentication);
		
		wapiTokenStore.storeAccessToken(wapiToken);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#readAccessToken(java.lang.String)
	 */
	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		DefaultOAuth2AccessToken token = null;
		
		logger.info("reading access token for value {}", tokenValue);
		
		IOAuth2AccessToken wapiToken = wapiTokenStore.readAccessTokenForValue(tokenValue);
		
		if(wapiToken != null) {
			token = new DefaultOAuth2AccessToken(wapiToken.getValue());

			logger.info("loaded token {} from persistence store", wapiToken);
			
			if(wapiToken.getAdditionalInformation() != null)
				token.setAdditionalInformation(wapiToken.getAdditionalInformation());
			token.setExpiration(wapiToken.getExpiration());
			if(wapiToken.getScope() != null)
				token.setScope(wapiToken.getScope());
			
			if(wapiToken.getRefreshToken() != null) {
				if(wapiToken.getRefreshToken().getExpiration() != null) {
					token.setRefreshToken(new DefaultExpiringOAuth2RefreshToken(wapiToken.getRefreshToken().getValue(), 
							wapiToken.getRefreshToken().getExpiration())); 
				} else {
					token.setRefreshToken(new DefaultOAuth2RefreshToken(wapiToken.getRefreshToken().getValue()));
				}		
			}
			
			if(wapiToken.getAuthentication() != null) {
			}
		}
		
		return token;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#removeAccessToken(org.springframework.security.oauth2.common.OAuth2AccessToken)
	 */
	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		logger.info("removing access token {}", token);
		
		wapiTokenStore.removeAccessToken(token.getValue());
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#storeRefreshToken(org.springframework.security.oauth2.common.OAuth2RefreshToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		logger.info("storing refresh token for token {} and authentication {}", refreshToken, authentication);

	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#readRefreshToken(java.lang.String)
	 */
	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		logger.info("reading refresh token for token value {}", tokenValue);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#readAuthenticationForRefreshToken(org.springframework.security.oauth2.common.OAuth2RefreshToken)
	 */
	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		logger.info("read authentication for refresh token {}", token);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#removeRefreshToken(org.springframework.security.oauth2.common.OAuth2RefreshToken)
	 */
	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		logger.info("remove refresh token {}", token);
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#removeAccessTokenUsingRefreshToken(org.springframework.security.oauth2.common.OAuth2RefreshToken)
	 */
	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		logger.info("remove access token using refresh token {}", refreshToken);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#getAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		logger.info("get access token for authentication {}", authentication);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#findTokensByClientIdAndUserName(java.lang.String, java.lang.String)
	 */
	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		logger.info("find tokens by client ID {} and user name {}", clientId, userName);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.TokenStore#findTokensByClientId(java.lang.String)
	 */
	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		logger.info("find tokens by client ID {}", clientId);
		
		return null;
	}


	private byte[] serializeObject(Serializable object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		
		oos.writeObject(object);
		
		oos.close();
		
		return baos.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Serializable> T deserializeObject(byte[] data) throws IOException, ClassNotFoundException {
		if(data != null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			
			try { 
				return (T)ois.readObject();
			} finally {
				ois.close();
			}
		} else
			return null;
	}
	
	private OAuth2Request restoreAuthentication(IOAuth2Request wr) {
		Set<? extends GrantedAuthority> authorities = (wr.getGrantedAuthorities() != null) 
				? wr.getGrantedAuthorities().stream().map((v) -> new FlowNinjaClientGrantedAuthority(v)).collect(Collectors.toSet())
				: null;
		Map<String, Serializable> extensions = new HashMap<String, Serializable>();
		
		if(wr.getExtensions() != null) {
			wr.getExtensions().forEach((n,v) -> {
				try {
					extensions.put(n, deserializeObject(v));
				} catch(Exception e) {
					logger.error("failed to deserilize object", e);
					throw new RuntimeException(e);
				};
			});
		}
		
		return new OAuth2Request(wr.getRequestParameters(), 
				wr.getClientId(), 
				authorities,
				wr.isApproved(), 
				wr.getScope(), 
				wr.getResourceIds(), 
				wr.getRedirectUri(), wr.getResponseTypes(), 
				extensions);		
	}
}
