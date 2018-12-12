package http;

import exceptions.AppException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection implements Closeable {

    private final HttpURLConnection connection;

    HttpConnection(String url) {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            throw new AppException(e);
        }
    }

    public InputStream getResponseAsInputStream() {
        responseCheck();
        try {
            return this.connection.getInputStream();
        } catch (IOException e) {
            throw new AppException(e);
        }
    }

    void responseCheck() throws AppException {
        try {
            int response = connection.getResponseCode();
            if (response != 200) {
                String msg = connection.getResponseMessage();
                throw new AppException(response + ": There was a problem with HTTP connection: " + msg);
            }
        } catch (IOException e) {
            throw new AppException(e);
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
            throw new AppException(e);
        }
        return response.toString();
    }
}
