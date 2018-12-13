package radar;

import data.Param;
import data.Sensor;
import data.Station;

import java.io.IOException;
import java.util.Date;

public interface RadarAdapter {
    Station findStationByName(String stationName) throws IOException;

    Sensor findSensor(Integer stationId, String paramName) throws IOException;

    Param findParamByName(String paramName);

    Date findDateByName(String date);
}
