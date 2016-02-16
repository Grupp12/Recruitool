package integration;

/**
 * Exception thrown when trying to persist an entity that already exists.
 */
public class EntityExistsException extends Exception {

	public EntityExistsException() {
	}

	public EntityExistsException(String message) {
		super(message);
	}
}
