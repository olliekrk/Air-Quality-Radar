package radar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import radar.GIOS.HttpExtractorGIOS;
import radar.GIOS.RadarReaderGIOS;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    @Ignore
    @Test
    public void loadCacheTest() {
        cacheLoader.loadCache(extractor, reader);
    }
}