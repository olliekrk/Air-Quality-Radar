package http;

//to extract input stream from http site, (to send http get)
public class PolishHttpExtractor implements HttpExtractor {
    private static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    private static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    private static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    @Override
    public String extractIndexData(Integer stationId) {
        String requestURL = String.format(AIR_QUALITY_INDEX_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractAllStationsData() {
        String requestURL = STATIONS_URL;
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractAllSensorsData(Integer stationId) {
        String requestURL = String.format(SENSORS_URL_TEMPLATE, stationId.toString());
        return connectAndExtract(requestURL);
    }

    @Override
    public String extractMeasurmentData(Integer sensorId) {
        String requestURL = String.format(DATA_URL_TEMPLATE, sensorId.toString());
        return connectAndExtract(requestURL);
    }

    private String connectAndExtract(String requestURL) {
        //try-with-resources
        try (HttpConnection connection = connectionFactory.build(requestURL)) {
            String response = connection.getResponseAsString();
            return response;
        }
    }

}
