package radar.polishGovApi;

import radar.AirQualityRadar;
import radar.cache.HttpExtractorGov;
import radar.cache.RadarPrinterGov;
import radar.cache.RadarTranslatorGov;

public class PolishRadar extends AirQualityRadar {

    public PolishRadar() {
        this.extractor = new HttpExtractorGov();
        this.adapter = new PolishRadarAdapter();
        this.printer = new RadarPrinterGov();
        this.translator = new RadarTranslatorGov();
    }
}
