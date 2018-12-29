package radar;

import exceptions.HttpConnectionException;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection implements Closeable {

    private final HttpURLConnection connection;

    HttpConnection(String url) {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
    }

    private void responseCheck() {
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

    public String getResponseAsString() {
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
