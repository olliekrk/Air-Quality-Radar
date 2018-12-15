import radar.AirQualityRadar;
import radar.polishGovApi.PolishRadar;

public class Main {
    public static void main(String[] args) {
        String todayDate = "2018-12-15";
        String example1Date = todayDate + " 01:00:00";
        String example2Date = todayDate + " 08:00:00";
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
        radar.getParamWithMinValueForDay("działoszyn", todayDate);
//        6
        radar.getNStationsWithMaxParamValueForDay(4, "pm10", todayDate);
//        7
        radar.getParamExtremeMeasurementValues("pm10");
    }
}
