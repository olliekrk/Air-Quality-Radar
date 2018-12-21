package radar;

public class CacheUser {

    private static boolean updatesAllowed = true;
    private final static String cachePath = "radarCache.json";

    private Cache cache;
    private CacheManager cacheManager;
    private CacheRadar radar;

    public CacheUser(CacheRadar radar) {
        this.radar = radar;
        this.cacheManager = new CacheManager(cachePath);
        this.prepareCache();
    }

    private void prepareCache() {
        cache = cacheManager.loadCache(radar.getExtractor(), radar.getTranslator(), updatesAllowed);
        this.radar.seeker = new CacheSeeker(cache);
    }

    public static boolean areUpdatesAllowed() {
        return updatesAllowed;
    }

    public static void setUpdatesAllowed(boolean updatesAllowed) {
        CacheUser.updatesAllowed = updatesAllowed;
    }

    public CacheRadar getRadar() {
        return radar;
    }
}

