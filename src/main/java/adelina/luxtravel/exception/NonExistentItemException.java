package adelina.luxtravel.exception;

/**
 * Custom exception for non-existing item
 */
public class NonExistentItemException extends RuntimeException {
    public NonExistentItemException(String message) {
        super(message);
    }
}
