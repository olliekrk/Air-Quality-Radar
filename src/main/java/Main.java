import radar.*;

import java.time.LocalDateTime;

public class Main {

    private static void cachedTest() {

        //!
        CacheUser cacheUser = new CacheUser(new CacheRadarGov());
        CacheRadar radar = cacheUser.getRadar();
        //!

        LocalDateTime date1 = DataAnalyzer.intoDateTime("2018-12-21 08:00:00");
        LocalDateTime date2 = DataAnalyzer.intoDateTime("2018-12-21 15:00:00");

        String[] names = new String[1];
        names[0] = "Działoszyn";

        try {
            radar.getAirQualityIndexForStation("Działoszyn");
            radar.getParamValueForStation("Działoszyn", "pm10", date1);
            radar.drawGraph(names, "pm10", date1, date2);
        } catch (MissingDataException | UnknownParameterException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        cachedTest();
    }
    /*
    TODO: wykres
    TODO: uruchamianie z IO
    TODO: jar
    TODO: testy - Mockito
    TODO: wzorzec projektowy
    TODO: jedna funkcjonalność kompatybilna z Airly
     */
}