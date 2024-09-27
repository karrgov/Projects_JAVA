package bg.sofia.uni.fmi.mjt.udemy.exception;

public class AccounNotFoundException extends Exception {
    public AccounNotFoundException(String message) {
        super(message);
    }

    public AccounNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
