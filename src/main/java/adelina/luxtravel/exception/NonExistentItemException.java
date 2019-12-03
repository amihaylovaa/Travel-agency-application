package adelina.luxtravel.exception;

public class NonExistentItemException extends RuntimeException {

    public NonExistentItemException(String message) {
        super(message);
    }
}
