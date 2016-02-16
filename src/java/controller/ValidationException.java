package controller;

/**
 * Exception thrown when data validation fails.
 */
public class ValidationException extends Exception {
	public ValidationException(String message) {
		super(message);
	}
}
