package radar.GIOS;

import radar.AirRadar;
import radar.CacheLoader;
import radar.CacheSeeker;

public class AirRadarGIOS extends AirRadar {
    AirRadarGIOS() {
        this.reader = new RadarReaderGIOS();
        this.extractor = new HttpExtractorGIOS();
        this.printer = new RadarPrinterGIOS();

        CacheLoader cacheLoader = new CacheLoader();
        cacheLoader.loadCache(this.extractor, this.reader);

        this.seeker = new CacheSeeker(cacheLoader.getCache());
    }
}
