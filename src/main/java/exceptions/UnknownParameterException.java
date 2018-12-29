package exceptions;

/**
 * Exception thrown when user gives an unknown parameter, i.e such which
 * does not have its equivalent in {@link data.ParamType}.
 */
public class UnknownParameterException extends Exception {
    public UnknownParameterException(String message) {
        super(message);
    }
}
