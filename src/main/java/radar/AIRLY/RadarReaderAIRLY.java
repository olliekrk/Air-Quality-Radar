package radar.AIRLY;

import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;
import radar.RadarReader;

import java.io.IOException;

public class RadarReaderAIRLY implements RadarReader {
    @Override
    public AirQualityIndex readIndexData(String data) throws IOException {
        return null;
    }

    @Override
    public MeasurementData readMeasurementData(String measurementData) {
        return null;
    }

    @Override
    public Sensor[] readSensorsData(String sensorsData) {
        return new Sensor[0];
    }

    @Override
    public Station[] readStationsData(String stationsData) {
        return new Station[0];
    }
}
