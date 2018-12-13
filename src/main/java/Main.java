import radar.AirQualityRadar;
import radar.PolishRadar;

public class Main {
    public static void main(String[] args) {
        AirQualityRadar radar = new PolishRadar();
//        1
//        radar.getAirQualityIndexForStation("Jaslo-Sikorskiego-WIOS");

//        2
           radar.getCurrentParamValueForStation("działoszyn","pm10");
    }
}
