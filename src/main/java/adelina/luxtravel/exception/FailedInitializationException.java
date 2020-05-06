package adelina.luxtravel.exception;

/**
 * Custom exception for invalid initialization
 */
public class FailedInitializationException extends RuntimeException {
    public FailedInitializationException(String message) {
        super(message);
    }
}
