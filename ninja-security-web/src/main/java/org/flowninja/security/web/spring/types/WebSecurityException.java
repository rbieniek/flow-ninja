/**
 * 
 */
package org.flowninja.security.web.spring.types;

/**
 * @author rainer
 *
 */
public class WebSecurityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4207498701802577769L;

	/**
	 * 
	 */
	public WebSecurityException() {
	}

	/**
	 * @param message
	 */
	public WebSecurityException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebSecurityException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WebSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WebSecurityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
