package radar;

import http.PolishHttpExtractor;

public class PolishRadar extends AirQualityRadar {

    public PolishRadar() {
        this.extractor = new PolishHttpExtractor();
        this.adapter = new PolishRadarAdapter();
        this.printer = new PolishRadarPrinter();
        this.translator = new PolishRadarTranslator();
    }
}
