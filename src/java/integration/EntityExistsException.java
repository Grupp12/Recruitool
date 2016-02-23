package integration;

/**
 * Exception thrown when trying to persist an entity that already exists.
 */
public class EntityExistsException extends Exception {

	/**
	 * @see java.lang.Exception#Exception()
	 */
	public EntityExistsException() {
	}

	/**
	 * @see java.lang.Exception#Exception(String)
	 */
	public EntityExistsException(String message) {
		super(message);
	}
}
