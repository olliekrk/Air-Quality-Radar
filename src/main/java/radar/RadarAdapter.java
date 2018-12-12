package radar;

import data.Param;
import data.Station;

import java.util.Date;

public interface RadarAdapter {
    Station findStationByName(String stationName);

    Param findParamByName(String paramName);

    Date findDateByName(String date);
}
