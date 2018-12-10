import java.io.IOException;

public class AppException extends RuntimeException {
    public AppException(String e) {
        super(e);
    }

    public AppException(IOException e) {
        super(e);
    }
}
