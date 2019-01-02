package radar;

import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;

import java.io.IOException;

/**
 * Interface used to parse extracted strings containing JSON data into POJO.
 */
public interface RadarReader {
    /**
     * Parses string JSON data containing air quality index into its object representation.
     *
     * @param data string JSON data
     * @return air quality index object
     * @throws IOException when JSON data has incorrect format
     */
    AirQualityIndex readIndexData(String data) throws IOException;

    /**
     * Parses string JSON data containing measurement data into its object representation.
     *
     * @param measurementData string JSON data
     * @return measurement data object
     */
    MeasurementData readMeasurementData(String measurementData);

    /**
     * Parses string JSON data containing list of sensors of some station into its object representation.
     *
     * @param sensorsData string JSON data
     * @return array of sensor objects
     */
    Sensor[] readSensorsData(String sensorsData);

    /**
     * Parses string JSON data containing list of stations into its object representation.
     *
     * @param stationsData string JSON data
     * @return array of station objects
     */
    Station[] readStationsData(String stationsData);
}
