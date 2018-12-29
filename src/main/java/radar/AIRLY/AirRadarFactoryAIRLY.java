package radar.AIRLY;

import radar.AirRadar;
import radar.AirRadarFactory;

/**
 * Factory-method class for creating Air Radar compatible with Airly API.
 * Part of "Factory" design pattern.
 */
public class AirRadarFactoryAIRLY implements AirRadarFactory {
    @Override
    public AirRadar createAirRadar() {
        return new AirRadarAIRLY();
    }
}
