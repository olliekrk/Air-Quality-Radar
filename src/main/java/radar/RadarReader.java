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
    AirQualityIndex readIndexData(String data) throws IOException;

    MeasurementData readMeasurementData(String measurementData);

    Sensor[] readSensorsData(String sensorsData);

    Station[] readStationsData(String stationsData);
}
