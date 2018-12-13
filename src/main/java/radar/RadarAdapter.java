package radar;

import data.MeasurementData;
import data.Param;
import data.Sensor;
import data.Station;
import http.HttpExtractor;

import java.io.IOException;
import java.util.Date;

public interface RadarAdapter {

    Station findStationByName(String stationName, HttpExtractor extractor) throws IOException;

    Sensor findSensor(Integer stationId, String paramName, HttpExtractor extractor) throws IOException;

    MeasurementData findData(String stationName, String paramName, HttpExtractor extractor, RadarTranslator translator) throws IOException;

    Param findParamByName(String paramName);

    Date findDateByName(String date);
}
