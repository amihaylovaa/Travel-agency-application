package adelina.luxtravel.exception;

/**
 * Custom exception thrown when an item does not exist
 */
public class NonExistentItemException extends RuntimeException {
    public NonExistentItemException(String message) {
        super(message);
    }
}
