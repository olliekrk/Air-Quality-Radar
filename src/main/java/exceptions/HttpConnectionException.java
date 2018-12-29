package exceptions;

public class HttpConnectionException extends RuntimeException {
    public HttpConnectionException(String message) {
        super(message);
    }
}
