package radar.polishGovApi;

import radar.AirQualityRadar;

public class PolishRadar extends AirQualityRadar {

    public PolishRadar() {
        this.extractor = new PolishHttpExtractor();
        this.adapter = new PolishRadarAdapter();
        this.printer = new PolishRadarPrinter();
        this.translator = new PolishRadarTranslator();
    }
}
