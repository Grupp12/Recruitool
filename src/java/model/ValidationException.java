package model;

/**
 * Exception thrown when data validation fails.
 */
public class ValidationException extends Exception {
	
	/**
	 * @see java.lang.Exception#Exception(String)
	 * @param message the error message.
	 */
	public ValidationException(String message) {
		super(message);
	}
}
