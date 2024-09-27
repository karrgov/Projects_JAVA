package bg.sofia.uni.fmi.mjt.csvprocessor.exceptions;

public class CsvDataNotCorrectException extends RuntimeException {

    public CsvDataNotCorrectException(String message) {
        super(message);
    }

    public CsvDataNotCorrectException(String message, Throwable cause) {
        super(message, cause);
    }

}
