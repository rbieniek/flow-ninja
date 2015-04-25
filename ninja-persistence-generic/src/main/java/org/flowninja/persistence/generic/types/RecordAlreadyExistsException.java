/**
 * 
 */
package org.flowninja.persistence.generic.types;

/**
 * This exception is thrown if an insert operation fails due to an unique record part already exists in the database
 * 
 * @author rainer
 *
 */
public class RecordAlreadyExistsException extends PersistenceServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -214845471746773108L;

	/**
	 * 
	 */
	public RecordAlreadyExistsException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RecordAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RecordAlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RecordAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
