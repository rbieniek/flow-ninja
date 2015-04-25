/**
 * 
 */
package org.flowninja.persistence.generic.types;

/**
 * This exception is thrown if an insert operation fails due to a referenced record does not exist in the database
 * 
 * @author rainer
 *
 */
public class RecordNotFoundException extends PersistenceServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -214845471746773108L;

	/**
	 * 
	 */
	public RecordNotFoundException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RecordNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RecordNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RecordNotFoundException(Throwable cause) {
		super(cause);
	}

}
