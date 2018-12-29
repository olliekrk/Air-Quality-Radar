package exceptions;

/**
 * Exception thrown by {@link radar.HttpExtractor} when there has been problems with establishing
 * HTTP connection with REST API.
 */
public class HttpConnectionException extends RuntimeException {
    public HttpConnectionException(String message) {
        super(message);
    }
}
