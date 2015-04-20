/**
 * 
 */
package org.flowninja.persistence.generic;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class PasswordHasherTest {

	@Test
	public void assertLength() {
		Assert.assertEquals(PersistenceConstants.PASSWORD_HASH_LENGTH,
				PasswordHasher.hash("foo@foo.com", "bar").length());
		Assert.assertEquals(PersistenceConstants.PASSWORD_HASH_LENGTH,
				PasswordHasher.hash("foo_foo_foo@foo.com", "bar_bar_bar").length());
	}
	
	@Test
	public void assertSame() {
		Assert.assertEquals(PasswordHasher.hash("foo@foo.com", "bar"), 
				PasswordHasher.hash("foo@foo.com", "bar"));
	}
	
	@Test
	public void assertDifferentMail() {
		Assert.assertFalse(PasswordHasher.hash("foo@foo.com", "bar")
				.equals(PasswordHasher.hash("foo2@foo.com", "bar")));		
	}
	
	@Test
	public void assertDifferentPassword() {
		Assert.assertFalse(PasswordHasher.hash("foo@foo.com", "bar")
				.equals(PasswordHasher.hash("foo@foo.com", "bar2")));		
	}
}
