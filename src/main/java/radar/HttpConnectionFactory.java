package radar;

public class HttpConnectionFactory {
    public HttpConnection build (String url){
        return new HttpConnection(url);
    }
}
