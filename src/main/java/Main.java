import radar.AirQualityRadar;
import radar.cache.CacheUser;
import radar.polishGovApi.PolishRadar;

public class Main {

    public static void noCacheTest() {
        String todayDate = "2018-12-19";
        String example1Date = todayDate + " 01:00:00";
        String example2Date = todayDate + " 08:00:00";
        AirQualityRadar radar = new PolishRadar();

//        1
        radar.getAirQualityIndexForStation("Kraków, Aleja Krasińskiego");
//        2
        radar.getCurrentParamValueForStation("Kraków, Aleja Krasińskiego", "pm10");
//        3
        radar.getAverageParamValueForPeriod("Kraków, Aleja Krasińskiego", "pm10", example1Date, example2Date);
//        4
        radar.getParamWithMaxAmplitudeForPeriod("Kraków, Aleja Krasińskiego", example1Date);
//        5
        radar.getParamWithMinValueForDay("Kraków, Aleja Krasińskiego", todayDate);
//        6
        radar.getNStationsWithMaxParamValueForDay(4, "so2", todayDate);
//        7
        radar.getParamExtremeMeasurementValues("so2");

    }

    public static void cachedTest() {
        AirQualityRadar radar = new PolishRadar();
        CacheUser cacheUser = new CacheUser(radar);
        cacheUser.useCache();
    }

    public static void main(String[] args) {
        cachedTest();
    }
}

