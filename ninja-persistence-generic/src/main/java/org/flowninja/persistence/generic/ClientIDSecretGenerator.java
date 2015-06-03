/**
 * 
 */
package org.flowninja.persistence.generic;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class ClientIDSecretGenerator {
	private static final Logger logger = LoggerFactory.getLogger(ClientIDSecretGenerator.class);
	
	public static class IDSecretPair {
		private String clientID;
		private String clientSecret;
		
		private IDSecretPair(String clientID, String clientSecret) {
			this.clientID = clientID;
			this.clientSecret = clientSecret;
		}

		/**
		 * @return the clientID
		 */
		public String getClientID() {
			return clientID;
		}

		/**
		 * @return the clientSecret
		 */
		public String getClientSecret() {
			return clientSecret;
		}
		
	}

	/**
	 * hidden constructor to prevent instantiation of this class
	 */
	private ClientIDSecretGenerator() {}
	
	public static IDSecretPair generatePair(AccountingGroupKey groupKey, CollectorKey collectoryKey) {
		try {
			LocalDateTime now = LocalDateTime.now();
			Charset cs = Charset.forName("UTF-8");
			
			String idText = String.format("%s:%s:%s", groupKey, StringUtils.remove(UUID.randomUUID().toString(), "-"), collectoryKey);
			String keyText = String.format("%s:%s:%s", StringUtils.remove(UUID.randomUUID().toString(), "-"), now.toString(), collectoryKey);
			
			MessageDigest idDigest = MessageDigest.getInstance("SHA-1");
			MessageDigest keyDigest = MessageDigest.getInstance("SHA-1");
			
			idDigest.update(idText.getBytes(cs));
			keyDigest.update(keyText.getBytes(cs));
			
			return new IDSecretPair(new String(Hex.encodeHex(idDigest.digest(), true)), Base64.encodeBase64String(keyDigest.digest()));
		} catch(Exception e) {
			logger.error("cannot generate client ID / secret pair", e);
			
			throw new RuntimeException(e);
		}
	}
}
