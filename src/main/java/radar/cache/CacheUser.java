package radar.cache;

import radar.AirQualityRadar;

public class CacheUser {

    //TODO:można pododwać flagi żeby nie aktualizować za każdym razem całego cache'a
    // tylko aktualizować to czego chce klient (wiemy po parametrach wywolania programu)

    private static boolean updatesAllowed = true;
    private static String cachePath = "radarCache.json";

    private Cache cache;
    private CacheManager cacheManager;
    private AirQualityRadar radar;

    public CacheUser(AirQualityRadar radar) {
        this.radar = radar;
        this.cache = null;
        this.cacheManager = new CacheManager(cachePath);
    }

    public void useCache() {
        cache = cacheManager.loadCache(radar.getExtractor(), radar.getTranslator(), updatesAllowed);
    }

    public static void setUpdatesAllowed(boolean updatesAllowed) {
        CacheUser.updatesAllowed = updatesAllowed;
    }

    public Cache getCache() {
        return cache;
    }
}

