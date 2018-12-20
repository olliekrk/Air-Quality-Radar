import radar.CacheRadar;
import radar.CacheRadarGov;
import radar.CacheUser;
import radar.MissingDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void cachedTest() {
        CacheUser cacheUser = new CacheUser(new CacheRadarGov());
        CacheRadar radar = cacheUser.getRadar();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String nowDate = dtf.format(now);

        String date1 = nowDate + " 01:00:00";
        String date2 = nowDate + " 13:20:00";

        String stationName = "Działoszyn";
        String param = "PM10";

        try {
            radar.getAirQualityIndexForStation(stationName);
            radar.getAverageParamValuePeriod(stationName, param, date1, date2);
            radar.getCurrentParamValueForStation(stationName, param);
            radar.getExtremeParamValuePeriod(stationName, date1, date2);
            radar.getParamOfMinimalValue(stationName, nowDate);
            radar.getExtremeParamValueWhereAndWhen(param);
            radar.getNSensorsWithMaximumParamValue(stationName, date1, param, 3);

        } catch (MissingDataException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        cachedTest();
    }
    /*
    TODO: 1. wykres
    TODO: 2. uruchamianie z IO
    TODO: 3. jar
    TODO: 4. testy - Mockito
    TODO: 5. wzorzec projektowy
    TODO: 6. serializacja?
    TODO: opisy funkcjonalności się zmieniają, sprawdzać
     */
}

