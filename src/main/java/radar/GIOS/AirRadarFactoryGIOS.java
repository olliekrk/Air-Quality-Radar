package radar.GIOS;

import radar.AirRadar;
import radar.AirRadarFactory;

public class AirRadarFactoryGIOS implements AirRadarFactory {
    @Override
    public AirRadar createAirRadar() {
        return new AirRadarGIOS();
    }
}
