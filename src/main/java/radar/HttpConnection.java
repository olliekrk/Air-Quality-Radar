package radar;

import exceptions.HttpConnectionException;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class used for establishing connection with API.
 * Part of "Factory" design pattern alongside with {@link HttpConnectionFactory}
 */
public class HttpConnection implements Closeable {

    /**
     * Connection established with HTTP server.
     */
    private final HttpURLConnection connection;

    /**
     * Constructor which opens connection with given URL address.
     *
     * @param url URL address to establish connection with.
     */
    HttpConnection(String url) throws HttpConnectionException {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
    }

    /**
     * Method called after every connection is made to make sure that server's response was positive.
     * Checks whether response code is 200.
     */
    private void responseCheck() throws HttpConnectionException {
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                String msg = connection.getResponseMessage();
                throw new HttpConnectionException(String.format("[%d]: There is some problem with HTTP connection!\n%s", responseCode, msg));
            }
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
    }

    /**
     * Returns response received from server after establishing connection.
     * Calls method to check whether connection was successful.
     *
     * @return server's response
     */
    public String getResponseAsString() throws HttpConnectionException {
        responseCheck();
        StringBuilder response = new StringBuilder();
        try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = responseReader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public void close() {
        connection.disconnect();
    }
}
