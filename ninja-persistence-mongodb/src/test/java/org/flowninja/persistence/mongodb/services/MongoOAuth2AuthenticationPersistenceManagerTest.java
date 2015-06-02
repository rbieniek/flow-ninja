package org.flowninja.persistence.mongodb.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;

import org.bson.types.Binary;
import org.fest.assertions.data.MapEntry;
import org.flowninja.persistence.generic.types.IOAuth2Authentication;
import org.flowninja.persistence.generic.types.IOAuth2Request;
import org.flowninja.persistence.generic.types.impl.OAuth2AuthenticationImpl;
import org.flowninja.persistence.generic.types.impl.OAuth2RequestImpl;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.flowninja.persistence.mongodb.repositories.MongoOAuth2AuthenticationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=MongoTestConfig.class)
public class MongoOAuth2AuthenticationPersistenceManagerTest {

	@Autowired
	private MongoOAuth2AuthenticationPersistenceManager persistence;
	
	@Autowired
	private MongoOAuth2AuthenticationRepository repository;
	
	@Test
	public void persistBasicAuthentication() {
		IOAuth2Authentication auth = basicAuthentication();
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistuthenticationSimpleFields() {
		IOAuth2Authentication auth = basicAuthentication();
		
		auth.getStoredRequest().setRedirectUri("http://foo.bar/boo");
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isEqualTo("http://foo.bar/boo");
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistAuthenticationWithUser() {
		IOAuth2Authentication auth = basicAuthentication();
		byte[] data = createData(0);
		
		auth.setAuthentication(data);
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isEqualTo(new Binary(data));
	}

	@Test
	public void persistAuthenticationWithExtensions() {
		IOAuth2Authentication auth = basicAuthentication();
		byte[] dataOne = createData(1);
		byte[] dataTwo = createData(2);
		
		auth.getStoredRequest().setExtensions(new HashMap<String, byte[]>());
		auth.getStoredRequest().getExtensions().put("one", dataOne);
		auth.getStoredRequest().getExtensions().put("two", dataTwo);
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).contains(MapEntry.entry("one", new Binary(dataOne))).contains(MapEntry.entry("two", new Binary(dataTwo))).hasSize(2);
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistBasicAuthenticationWithGrantedAuthorities() {
		IOAuth2Authentication auth = basicAuthentication();
		
		auth.getStoredRequest().setGrantedAuthorities(Sets.newHashSet("one", "two"));
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).containsOnly("one", "two");
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistBasicAuthenticationWithRequestParameters() {
		IOAuth2Authentication auth = basicAuthentication();

		auth.getStoredRequest().setRequestParameters(new HashMap<>());
		auth.getStoredRequest().getRequestParameters().put("one", "alpha");
		auth.getStoredRequest().getRequestParameters().put("two", "beta");
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).contains(MapEntry.entry("one", "alpha")).contains(MapEntry.entry("two", "beta")).hasSize(2);
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistBasicAuthenticationWithResourceIds() {
		IOAuth2Authentication auth = basicAuthentication();
		
		auth.getStoredRequest().setResourceIds(Sets.newHashSet("one", "two"));
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).containsOnly("one", "two");
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistBasicAuthenticationWithResponseTypes() {
		IOAuth2Authentication auth = basicAuthentication();
		
		auth.getStoredRequest().setResponseTypes(Sets.newHashSet("one", "two"));
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).containsOnly("one", "two");
		assertThat(pma.getScope()).isNull();
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void persistBasicAuthenticationWithScope() {
		IOAuth2Authentication auth = basicAuthentication();
		
		auth.getStoredRequest().setScope(Sets.newHashSet("one", "two"));
		
		MongoOAuth2Authentication ma = persistence.persistWapiAuthentication(auth);
		
		assertThat(ma).isNotNull();
		assertThat(ma.getId()).isNotNull();
		
		MongoOAuth2Authentication pma = repository.findOne(ma.getId());
		
		assertThat(pma).isNotNull();
		assertThat(pma.getClientId()).isEqualTo("foo@bar.com");
		assertThat(pma.getExtensions()).isNull();
		assertThat(pma.getGrantedAuthorities()).isNull();
		assertThat(pma.getRedirectUri()).isNull();
		assertThat(pma.getRequestParameters()).isNull();
		assertThat(pma.getResourceIds()).isNull();
		assertThat(pma.getResponseTypes()).isNull();
		assertThat(pma.getScope()).containsOnly("one", "two");
		assertThat(pma.getUserAuthentication()).isNull();
	}

	@Test
	public void restoreBasicAuthentication() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithExtensions() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		byte[] dataOne = createData(1);
		byte[] dataTwo = createData(2);
		
		auth.setExtensions(new HashMap<>());
		auth.getExtensions().put("one", new Binary(dataOne));
		auth.getExtensions().put("two", new Binary(dataTwo));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).contains(MapEntry.entry("one", dataOne), MapEntry.entry("two", dataTwo)).hasSize(2);
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithGrantedAuthorities() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();

		auth.setGrantedAuthorities(Lists.newArrayList("one", "two"));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).containsOnly("one", "two");
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithRedirectUri() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		
		auth.setRedirectUri("http://localhost/");
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isEqualTo("http://localhost/");
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithRequestParameters() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();

		auth.setRequestParameters(new HashMap<>());
		auth.getRequestParameters().put("one", "alpha");
		auth.getRequestParameters().put("two", "beta");
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).contains(MapEntry.entry("one", "alpha"), MapEntry.entry("two", "beta")).hasSize(2);
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithResourceIds() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();

		auth.setResourceIds(Lists.newArrayList("one", "two"));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).containsOnly("one", "two");
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithResponseTypes() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		
		auth.setResponseTypes(Lists.newArrayList("one", "two"));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).containsOnly("one", "two");
		assertThat(req.getScope()).isNull();
	}

	@Test
	public void restoreAuthenticationWithScopes() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		
		auth.setScope(Lists.newArrayList("one", "two"));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isNull();
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).containsOnly("one", "two");
	}
	
	@Test
	public void restoreAuthenticationWithUser() {
		MongoOAuth2Authentication auth = basicMongoAuthentication();
		byte[] data = createData(0);

		auth.setUserAuthentication(new Binary(data));
		
		IOAuth2Authentication wa = persistence.restoreAuthentication(auth);
		
		assertThat(wa).isNotNull();
		assertThat(wa.getAuthentication()).isEqualTo(data);
		assertThat(wa.getStoredRequest()).isNotNull();
	
		IOAuth2Request req = wa.getStoredRequest();
		
		assertThat(req.getClientId()).isEqualTo("foo@bar.com");
		assertThat(req.isApproved()).isTrue();
		assertThat(req.getExtensions()).isNull();
		assertThat(req.getGrantedAuthorities()).isNull();
		assertThat(req.getRedirectUri()).isNull();
		assertThat(req.getRequestParameters()).isNull();
		assertThat(req.getResourceIds()).isNull();
		assertThat(req.getResponseTypes()).isNull();
		assertThat(req.getScope()).isNull();;
	}

	private IOAuth2Authentication basicAuthentication() {
		OAuth2AuthenticationImpl auth = new OAuth2AuthenticationImpl();
		OAuth2RequestImpl request = new OAuth2RequestImpl();
		
		request.setApproved(true);
		request.setClientId("foo@bar.com");
		
		auth.setStoredRequest(request);
		
		return auth;
	}

	public MongoOAuth2Authentication basicMongoAuthentication() {
		MongoOAuth2Authentication auth = new MongoOAuth2Authentication();
		
		auth.setApproved(true);
		auth.setClientId("foo@bar.com");
		
		return auth;
	}

	private byte[] createData(int base) {
		byte[] data = new byte[32768];
		
		for(int i=0; i<data.length; i++) 
			data[i] = (byte)((base + i) % 256);

		return data;
	}

}
