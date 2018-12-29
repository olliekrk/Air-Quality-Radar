package radar;

import exceptions.HttpConnectionException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpConnectionTest {

    private final String invalidUrl = "http://api.gios.gov.pl/pjp-api/rest/data/getData/0";
    private final String invalidUrl2 = "http://api.gios.gov.pl/pjp-api/rest/data/getData/";
    private final String validUrl = "http://api.gios.gov.pl/pjp-api/rest/data/getData/92";

    @Test(expected = HttpConnectionException.class)
    public void connectionFailsTest() {
        HttpConnection connection = new HttpConnection(invalidUrl);
        System.out.println(connection.getResponseAsString());
    }

    @Test(expected = HttpConnectionException.class)
    public void connectionFailsTest2() {
        HttpConnection connection = new HttpConnection(invalidUrl2);
        System.out.println(connection.getResponseAsString());
    }

    @Test
    public void connectionSuccessesTest() {
        HttpConnection connection = new HttpConnection(validUrl);
        String response = connection.getResponseAsString();
        connection.close();
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }
}