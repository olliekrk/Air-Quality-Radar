package radar.polishGovApi;

import data.MeasurementData;
import data.Sensor;
import data.Station;
import radar.cache.HttpExtractor;
import radar.RadarAdapter;
import radar.cache.RadarTranslator;

import java.io.IOException;


//do szukania odpowiednich obiektow po ich stringowych id, przy pomocy extractora
public class PolishRadarAdapter implements RadarAdapter {
    //these methods work typically with polish API
    //to make radar work with other API it is needed to create another class implementing RadarAdapter interface

    @Override
    public Station findStationByName(String stationName, HttpExtractor extractor, RadarTranslator translator) throws IOException {
        String allStationsData = extractor.extractAllStationsData();
        Station[] stations = translator.readStationsData(allStationsData);
        for (Station station : stations) {
            if (station.getStationName().compareToIgnoreCase(stationName) == 0) {
                return station;
            }
        }
        throw new IOException("Could not find station named: " + stationName);
    }

    @Override
    public Sensor findSensor(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException {
        String allSensorsData = extractor.extractAllSensorsData(stationId);
        Sensor[] sensors = translator.readSensorsData(allSensorsData);
        for (Sensor sensor : sensors) {
            if (sensor.getParam().getParamCode().compareToIgnoreCase(paramName) == 0) {
                return sensor;
            }
        }
        throw new IOException("Could not find " + paramName + " sensor at station with ID: " + stationId);
    }

    @Override
    public MeasurementData findData(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException {
        Sensor sensor = this.findSensor(stationId, paramName, extractor, translator);
        String measurementData = extractor.extractMeasurementData(sensor.getId());
        return translator.readMeasurementData(measurementData);
    }
}
