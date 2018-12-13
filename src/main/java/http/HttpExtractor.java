package http;

public interface HttpExtractor {

    String extractIndexData(Integer stationId);

    String extractAllStationsData();

    String extractAllSensorsData(Integer stationId);

    String extractMeasurmentData(Integer sensorId);
}