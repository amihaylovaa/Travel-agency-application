package adelina.luxtravel.exception;

/**
 * Custom exception when mismatch between passwords occurs
 */
public class MismatchException extends RuntimeException {
    public MismatchException(String message) {
        super(message);
    }
}
