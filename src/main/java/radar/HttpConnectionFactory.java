package radar;

/**
 * "Factory" design pattern alongside with {@link HttpConnection}.
 * One-method class used to establish HTTP connection with given URL.
 */
public class HttpConnectionFactory {
    /**
     * Makes a HTTP connection with given URL.
     *
     * @param url address to connect with
     * @return {@link HttpConnection} connection with given URL
     */
    public HttpConnection build(String url) {
        return new HttpConnection(url);
    }
}
