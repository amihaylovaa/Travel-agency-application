package adelina.luxtravel.exception;

/**
 * Custom exception for unsuccessful initialization of an object
 */
public class FailedInitializationException extends RuntimeException {
    public FailedInitializationException(String message) {
        super(message);
    }
}
