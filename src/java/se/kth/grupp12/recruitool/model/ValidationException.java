package se.kth.grupp12.recruitool.model;

/**
 * Exception thrown when data validation fails.
 */
public class ValidationException extends Exception {
	
	/**
	 * @see java.lang.Exception#Exception(String)
	 */
	public ValidationException(String message) {
		super(message);
	}
}
