package radar.AIRLY;

import radar.AirRadar;
import radar.AirRadarFactory;

public class AirRadarFactoryAIRLY implements AirRadarFactory {
    @Override
    public AirRadar createAirRadar() {
        return new AirRadarAIRLY();
    }
}
