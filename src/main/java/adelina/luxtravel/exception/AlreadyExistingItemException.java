package adelina.luxtravel.exception;

/**
 * Custom exception thrown when an item already exists
 */
public class AlreadyExistingItemException extends RuntimeException {
    public AlreadyExistingItemException(String message) {
        super(message);
    }
}
