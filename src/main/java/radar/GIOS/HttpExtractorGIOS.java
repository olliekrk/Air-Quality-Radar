package radar.GIOS;

import exceptions.HttpConnectionException;
import radar.HttpConnection;
import radar.HttpConnectionFactory;
import radar.HttpExtractor;

/**
 * Class implementing {@link HttpExtractor} interface, which methods
 * work typically with GIOś API.
 * Part of "Strategy" design pattern.
 */
public class HttpExtractorGIOS implements HttpExtractor {
    /**
     * URL to extract stations data from HTTP REST service.
     */
    static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    /**
     * URL to extract sensors data from HTTP REST service.
     */
    static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    /**
     * URL to extract measurement data from HTTP REST service.
     */
    static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    /**
     * URL to extract air quality index data from HTTP REST service.
     */
    static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    /**
     * Connection factory used to establish connection with HTTP REST service.
     */
    private HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();

    @Override
    public String extractIndexData(Integer stationId) throws HttpConnectionException {
        String requestURL = String.format(AIR_QUALITY_INDEX_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractAllStationsData() throws HttpConnectionException {
        return connectAndExtract(STATIONS_URL);
    }

    @Override
    public String extractAllSensorsData(Integer stationId) throws HttpConnectionException {
        String requestURL = String.format(SENSORS_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractMeasurementData(Integer sensorId) throws HttpConnectionException {
        String requestURL = String.format(DATA_URL_TEMPLATE, sensorId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String connectAndExtract(String requestURL) throws HttpConnectionException {
        try (HttpConnection connection = httpConnectionFactory.build(requestURL)) {
            return connection.getResponseAsString();
        }
    }

    //for tests
    void setHttpConnectionFactory(HttpConnectionFactory httpConnectionFactory) {
        this.httpConnectionFactory = httpConnectionFactory;
    }
}
