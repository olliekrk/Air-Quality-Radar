package radar;

import data.MeasurementData;
import data.Sensor;
import data.Station;
import data.AirQualityIndex;

import java.io.IOException;

public interface RadarTranslator {
    AirQualityIndex readIndexData(String data) throws IOException;

    MeasurementData readMeasurementData(String measurementData);

    Sensor[] readSensorsData(String sensorsData);

    Station[] readStationsData(String stationsData);
}
