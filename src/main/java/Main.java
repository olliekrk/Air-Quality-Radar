import radar.AirQualityRadar;
import radar.PolishRadar;

public class Main {
    public static void main(String[] args) {
        AirQualityRadar radar = new PolishRadar();
        radar.getAirQualityIndexForStation("działoszyn");
    }
}
