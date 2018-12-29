package radar.AIRLY;

import radar.AirRadar;

/**
 * Class which sets up AirRadar to be compatible with Airly API.
 * Part of "Strategy" and "Factory" design patterns.
 */
class AirRadarAIRLY extends AirRadar {
    AirRadarAIRLY() {
        this.reader = new RadarReaderAIRLY();
        this.extractor = new HttpExtractorAIRLY();
    }
}
