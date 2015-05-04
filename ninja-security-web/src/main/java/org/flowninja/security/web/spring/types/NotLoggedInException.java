/**
 * 
 */
package org.flowninja.security.web.spring.types;

/**
 * @author rainer
 *
 */
public class NotLoggedInException extends WebSecurityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5941356542002070892L;

	/**
	 * 
	 */
	public NotLoggedInException() {
	}

	/**
	 * @param message
	 */
	public NotLoggedInException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotLoggedInException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotLoggedInException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NotLoggedInException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
