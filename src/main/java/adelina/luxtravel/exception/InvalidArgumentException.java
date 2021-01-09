package adelina.luxtravel.exception;

/**
 * Custom exception thrown mainly when a method's arguments are not valid
 */
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
