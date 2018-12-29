package exceptions;

/**
 * Exception thrown by {@link radar.CacheSeeker} and {@link radar.AirRadar}
 * when there is no sufficient data available to extract information.
 */
public class MissingDataException extends Exception {
    public MissingDataException(String message) {
        super(message);
    }
}
