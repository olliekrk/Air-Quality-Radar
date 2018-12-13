package radar;

import data.MeasurementData;
import data.Sensor;
import data.qualityIndex.AirQualityIndex;

import java.io.IOException;

public interface RadarTranslator {
    AirQualityIndex readIndexData(String data) throws IOException;

    MeasurementData readMeasurementData(String sensorData);

    Sensor[] readSensorsData(String sensorsData);
}
