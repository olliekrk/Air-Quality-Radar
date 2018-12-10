public class App {
    private static final String STATIONS_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private static final String SENSORS_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/%s";
    private static final String DATA_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/data/getData/%s";
    private static final String AIR_QUALITY_INDEX_URL_TEMPLATE = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/%s";
    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) {
        String requestURL = String.format(SENSORS_URL_TEMPLATE, 14);

        //try-with-resources
        try(HttpConnection connection = new ConnectionFactory().build(requestURL)){
            String response = connection.response();
            System.out.println(response);
        }
    }
}