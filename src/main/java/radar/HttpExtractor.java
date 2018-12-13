package radar;

public interface HttpExtractor {

    String extractIndexData(Integer stationId);

    String extractAllStationsData();

    String extractAllSensorsData(Integer stationId);

    String extractMeasurementData(Integer sensorId);
}