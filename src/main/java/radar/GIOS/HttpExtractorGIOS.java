package radar.GIOS;

import radar.HttpConnection;
import radar.HttpConnectionFactory;
import radar.HttpExtractor;

/**
 * Class implementing {@link HttpExtractor} interface, which methods
 * work typically with Polish Gov API.
 */
public class HttpExtractorGIOS implements HttpExtractor {
    private static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    private static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    private static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    private final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();

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

}
