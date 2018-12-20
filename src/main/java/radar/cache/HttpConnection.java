package radar.cache;

import java.io.*;
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

    public InputStream getResponseAsInputStream() {
        responseCheck();
        try {
            return this.connection.getInputStream();
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
    }

    void responseCheck() {
        try {
            int response = connection.getResponseCode();
            if (response != 200) {
                String msg = connection.getResponseMessage();
                throw new HttpConnectionException(response + ": There was a problem with HTTP connection: " + msg);
            }
        } catch (IOException e) {
            throw new HttpConnectionException(e.getMessage());
        }
    }

    @Override
    public void close() {
        connection.disconnect();
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
}
