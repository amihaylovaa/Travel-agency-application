package adelina.luxtravel.exception;

/**
 * Custom exception for invalid object field
 */
public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
