package http;

import data.Station;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    private static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    private static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) {
        String requestURL = String.format(STATIONS_URL);
        List<Station> stations = new ArrayList<>();
        InputStream inputStream;

        //try-with-resources
        try (HttpConnection connection = new ConnectionFactory().build(requestURL)) {
            inputStream = connection.getResponseAsInputStream();
            connection.close();
        }


    }
}