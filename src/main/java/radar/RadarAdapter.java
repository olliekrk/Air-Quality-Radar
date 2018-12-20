package radar;

import data.MeasurementData;
import data.Param;
import data.Sensor;
import data.Station;

import java.io.IOException;
import java.util.Date;

public interface RadarAdapter {

    Station findStationByName(String stationName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

    Sensor findSensor(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

    MeasurementData findData(Integer stationId, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

}
