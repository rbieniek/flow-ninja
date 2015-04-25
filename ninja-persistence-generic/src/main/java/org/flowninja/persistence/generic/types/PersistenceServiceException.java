/**
 * 
 */
package org.flowninja.persistence.generic.types;

/**
 * Base class for all service exceptions thrown by the persistence service.
 * 
 * This base class is derived from {@link RuntimeException} in order to keep the signature lean
 * 
 * @author rainer
 * @see RuntimeException
 *
 */
public class PersistenceServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2389534386470738302L;

	public PersistenceServiceException() {
	}

	public PersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceServiceException(String message) {
		super(message);
	}

	public PersistenceServiceException(Throwable cause) {
		super(cause);
	}

}
