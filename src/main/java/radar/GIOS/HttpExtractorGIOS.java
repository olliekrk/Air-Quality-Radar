package radar.GIOS;

import radar.HttpConnection;
import radar.HttpConnectionFactory;
import radar.HttpExtractor;

/**
 * Class implementing {@link HttpExtractor} interface, which methods
 * work typically with Polish Gov GIOS API.
 */
public class HttpExtractorGIOS implements HttpExtractor {
    static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    private HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();

    @Override
    public String extractIndexData(Integer stationId) {
        String requestURL = String.format(AIR_QUALITY_INDEX_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractAllStationsData() {
        return connectAndExtract(STATIONS_URL);
    }

    @Override
    public String extractAllSensorsData(Integer stationId) {
        String requestURL = String.format(SENSORS_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractMeasurementData(Integer sensorId) {
        String requestURL = String.format(DATA_URL_TEMPLATE, sensorId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String connectAndExtract(String requestURL) {
        try (HttpConnection connection = httpConnectionFactory.build(requestURL)) {
            return connection.getResponseAsString();
        }
    }

    //for tests
    void setHttpConnectionFactory(HttpConnectionFactory httpConnectionFactory) {
        this.httpConnectionFactory = httpConnectionFactory;
    }
}
