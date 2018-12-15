import radar.AirQualityRadar;
import radar.polishGovApi.PolishRadar;

public class Main {
    public static void main(String[] args) {
        String todaysDate = "2018-12-15";
        String example1Date = todaysDate + " 01:00:00";
        String example2Date = todaysDate + " 08:00:00";
        AirQualityRadar radar = new PolishRadar();
//        1
        radar.getAirQualityIndexForStation("działoszyn");
//        2
        radar.getCurrentParamValueForStation("działoszyn", "pm10");
//        3
        radar.getAverageParamValueForPeriod("działoszyn", "pm10", example1Date, example2Date);
//        4
        radar.getParamWithMaxAmplitudeForPeriod("działoszyn", example1Date);
//        5
        radar.getParamWithMinValueForDay("działoszyn", todaysDate);
//        6
        radar.getNStationsWithMaxParamValueForDay(4, "so2", todaysDate);
//        7

    }
}
