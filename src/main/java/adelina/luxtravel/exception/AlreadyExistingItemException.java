package adelina.luxtravel.exception;

/**
 * Custom exception for already existing item
 */
public class AlreadyExistingItemException extends RuntimeException {
    public AlreadyExistingItemException(String message) {
        super(message);
    }
}
