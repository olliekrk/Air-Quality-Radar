package radar.polishGovApi;

import com.google.gson.Gson;
import data.MeasurementData;
import data.Param;
import data.Sensor;
import data.Station;
import radar.HttpExtractor;
import radar.RadarAdapter;
import radar.RadarTranslator;

import java.io.IOException;
import java.util.Date;


//do szukania odpowiednich obiektow po ich stringowych id, przy pomocy extractora
public class PolishRadarAdapter implements RadarAdapter {
    //these methods work typically with polish API
    //to make radar work with other API it is needed to create another class implementing RadarAdapter interface

    @Override
    public Station findStationByName(String stationName, HttpExtractor extractor) throws IOException {
        String allStations = extractor.extractAllStationsData();
        Gson gson = new Gson();
        Station[] stations = gson.fromJson(allStations, Station[].class);
        for (Station station : stations) {
            if (station.getStationName().compareToIgnoreCase(stationName) == 0) {
                return station;
            }
        }
        throw new IOException("Could not find station named: " + stationName);
    }

    @Override
    public Sensor findSensor(Integer stationId, String paramName, HttpExtractor extractor) throws IOException {
        String allSensors = extractor.extractAllSensorsData(stationId);
        Sensor[] sensors = new Gson().fromJson(allSensors, Sensor[].class);
        for (Sensor sensor : sensors) {
            if (sensor.getParam().getParamCode().compareToIgnoreCase(paramName) == 0) {
                return sensor;
            }
        }
        throw new IOException("Could not find " + paramName + " sensor at station with ID: " + stationId);
    }

    @Override
    public MeasurementData findData(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException {
        Sensor sensor = this.findSensor(stationId, paramName, extractor);
        String measurementData = extractor.extractMeasurementData(sensor.getId());
        MeasurementData measurement = translator.readMeasurementData(measurementData);
        return measurement;
    }

    @Override
    public Param findParamByName(String paramName) {
        return null;
    }

    @Override
    public Date findDateByName(String date) {
        return null;
    }
}
