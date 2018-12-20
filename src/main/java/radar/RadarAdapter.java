package radar;

import data.MeasurementData;
import data.Sensor;
import data.Station;
import radar.cache.HttpExtractor;
import radar.cache.RadarTranslator;

import java.io.IOException;

public interface RadarAdapter {

    Station findStationByName(String stationName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

    Sensor findSensor(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

    MeasurementData findData(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

}
