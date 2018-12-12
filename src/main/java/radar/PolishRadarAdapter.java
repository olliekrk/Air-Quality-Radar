package radar;

import com.google.gson.Gson;
import data.Param;
import data.Station;
import http.HttpExtractor;
import http.PolishHttpExtractor;

import java.io.IOException;
import java.util.Date;


//do parsowania (szukania) odpowiednich obiektow po ich stringowych id
public class PolishRadarAdapter implements RadarAdapter {

    private HttpExtractor extractor = new PolishHttpExtractor();

    @Override
    public Station findStationByName(String stationName) throws IOException {
        String allStations = extractor.extractAllStationsData();
        Gson gson = new Gson();
        Station[] stations = gson.fromJson(allStations, Station[].class);
        for (Station station : stations) {
            if (station.getStationName().compareToIgnoreCase(stationName) == 0) {
                return station;
            }
        }
        throw new IOException();
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
