package radar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import radar.GIOS.HttpExtractorGIOS;
import radar.GIOS.RadarReaderGIOS;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CacheLoaderTest {

    private HttpExtractor extractor;
    private RadarReader reader;
    private CacheLoader cacheLoader;
    private Cache cache;

    @Before
    public void setUp() {
        cacheLoader = new CacheLoader();
        cache = Mockito.mock(Cache.class);
        cacheLoader.setCache(cache);
        extractor = new HttpExtractorGIOS();
        reader = new RadarReaderGIOS();
    }

    @Test
    public void loadCacheNoUpdateTest() {
        when(cache.getUpdateDate()).thenReturn(LocalDateTime.now());
        assertFalse(cacheLoader.needsUpdate());
    }

    @Test
    public void loadCacheUpdateTest() {
        when(cache.getUpdateDate()).thenReturn(LocalDateTime.now().minusDays(1));
        assertTrue(cacheLoader.needsUpdate());
    }

    @Test
    public void loadCacheTestWithNoUpdate() {
        cacheLoader.loadCache(extractor, reader, false);
        assertNotNull(cacheLoader.getCache());
        assertTrue(cacheLoader.getCache().getUpdateDate().isBefore(LocalDateTime.now()));
    }

    @Ignore
    @Test
    public void loadCacheTestWithUpdate() {
        cacheLoader.loadCache(extractor, reader, false);
        LocalDateTime d1 = cacheLoader.getCache().getUpdateDate();

        cacheLoader.loadCache(extractor, reader, true);
        assertNotNull(cacheLoader.getCache());
        LocalDateTime d2 = cacheLoader.getCache().getUpdateDate();

        assertTrue(d1.isEqual(d2) || d1.isBefore(d2));
    }
}