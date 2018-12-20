package radar;

/**
 * Interface used to extract JSON data from HTTP REST service.
 */
public interface HttpExtractor {

    String extractAllStationsData();

    String extractAllSensorsData(Integer stationId);

    String extractMeasurementData(Integer sensorId);

    String extractIndexData(Integer stationId);

    String connectAndExtract(String requestURL);
}