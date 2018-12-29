package radar.AIRLY;

import radar.AirRadar;

public class AirRadarAIRLY extends AirRadar {
    AirRadarAIRLY() {
        this.reader = new RadarReaderAIRLY();
        this.extractor = new HttpExtractorAIRLY();
    }
}
