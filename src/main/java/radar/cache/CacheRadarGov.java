package radar.cache;

public class CacheRadarGov extends CacheRadar {
    public CacheRadarGov() {
        this.extractor = new HttpExtractorGov();
        this.printer = new RadarPrinterGov();
        this.translator = new RadarTranslatorGov();
        this.seeker = null;
    }
}
