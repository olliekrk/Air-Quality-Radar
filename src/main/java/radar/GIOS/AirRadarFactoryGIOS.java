package radar.GIOS;

import radar.AirRadar;
import radar.AirRadarFactory;

/**
 * Factory-method class for creating Air Radar compatible with GIOś API.
 * Part of "Factory" design pattern.
 */
public class AirRadarFactoryGIOS implements AirRadarFactory {
    @Override
    public AirRadar createAirRadar() {
        return new AirRadarGIOS();
    }
}
