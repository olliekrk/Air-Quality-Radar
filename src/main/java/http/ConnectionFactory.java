package http;

public class ConnectionFactory {
    public HttpConnection build (String url){
        return new HttpConnection(url);
    }
}
