package radar;

/**
 * Interface used to extract JSON data as strings from HTTP REST service.
 */
public interface HttpExtractor {

    /**
     * Returns JSON data as string, containing data about every station supported by the API.
     *
     * @return string data about all stations
     */
    String extractAllStationsData();

    /**
     * Returns JSON data as string, containing information about all sensors
     * located on station of given station's id.
     *
     * @param stationId id of a station
     * @return string data about all station's sensors
     */
    String extractAllSensorsData(Integer stationId);

    /**
     * Returns JSON data as string, containing measurement data from sensor
     * of given sensor's id.
     *
     * @param sensorId id of a sensor
     * @return string measurement data collected by that sensor
     */
    String extractMeasurementData(Integer sensorId);

    /**
     * Returns JSON data as string, containing air quality index of station of given station's id.
     *
     * @param stationId id of a station
     * @return air quality index data as string
     */
    String extractIndexData(Integer stationId);

    /**
     * Makes connection with right HTTP service and gets server's response for given URL request.
     *
     * @param requestURL URL from which data should be extracted
     * @return extracted data as string
     */
    String connectAndExtract(String requestURL);
}