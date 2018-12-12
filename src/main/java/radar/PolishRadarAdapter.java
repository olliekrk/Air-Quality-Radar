package radar;

import data.Param;
import data.Station;

import java.util.Date;


//do parsowania (szukania) odpowiednich obiektow po ich stringowych id
public class PolishRadarAdapter implements RadarAdapter {

    @Override
    public Station findStationByName(String stationName) {
        return null;
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
