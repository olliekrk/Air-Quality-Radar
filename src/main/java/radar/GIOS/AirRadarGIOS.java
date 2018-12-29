package radar.GIOS;

import radar.AirRadar;
import radar.CacheLoader;
import radar.CacheSeeker;

/**
 * Class which sets up AirRadar to be compatible with GIOś API.
 * Part of "Strategy" and "Factory" design patterns.
 */
class AirRadarGIOS extends AirRadar {
    AirRadarGIOS() {
        this.reader = new RadarReaderGIOS();
        this.extractor = new HttpExtractorGIOS();
        this.printer = new RadarPrinterGIOS();

        CacheLoader cacheLoader = new CacheLoader();
        cacheLoader.loadCache(this.extractor, this.reader);

        this.seeker = new CacheSeeker(cacheLoader.getCache());
    }
}
