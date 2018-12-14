import radar.AirQualityRadar;
import radar.polishGovApi.PolishRadar;

public class Main {
    public static void main(String[] args) {
        AirQualityRadar radar = new PolishRadar();
//        1
        radar.getAirQualityIndexForStation("działoszyn");
//        2
        radar.getCurrentParamValueForStation("działoszyn", "pm10");
//        3
        String example1Date = "2018-12-11 01:00:00";
        String example2Date = "2018-12-13 08:00:00";
        radar.getAverageParamValueForPeriod("działoszyn", "pm10", example1Date, example2Date);
//        4
        radar.getParamWithMaxAmplitudeForPeriod("działoszyn", example1Date);
//        5
        radar.getParamWithMinValueForDay("działoszyn", "2018-12-14");
    }
}
