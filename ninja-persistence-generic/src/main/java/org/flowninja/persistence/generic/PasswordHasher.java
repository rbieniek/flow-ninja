/**
 * 
 */
package org.flowninja.persistence.generic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Build a one.way password hash to be stored in a database
 * 
 * @author rainer
 *
 */
public class PasswordHasher {
	private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);	

	/**
	 * Build the secure hash from a given email address and password
	 * 
	 * @param emailAddress
	 * @param password
	 * @return
	 */
	public static String hash(String emailAddress, String password) {
		try {
			MessageDigest md;
			
			md = MessageDigest.getInstance("SHA-256");

			if(StringUtils.isNoneBlank(emailAddress))
				md.update(emailAddress.getBytes());
			md.update((byte)':');
			if(StringUtils.isNoneBlank(password))
				md.update(password.getBytes());
			
			return new String(Base64.encodeBase64(md.digest()));
		} catch (NoSuchAlgorithmException e) {
			logger.error("Failed to hash password", e);
			
			throw new RuntimeException(e);
		}
		
	}
}
