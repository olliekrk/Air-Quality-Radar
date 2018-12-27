package radar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import radar.GIOS.HttpExtractorGIOS;
import radar.GIOS.RadarReaderGIOS;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class CacheLoaderTest {

    private HttpExtractor extractor;
    private RadarReader reader;
    private CacheLoader cacheLoader;
    private Cache cache;

    @Before
    public void setUp() {
        cacheLoader = Mockito.mock(CacheLoader.class);
        cache = Mockito.mock(Cache.class);
        cacheLoader.setCache(cache);
        extractor = new HttpExtractorGIOS();
        reader = new RadarReaderGIOS();
    }

    //todo finish this
    @Test
    public void loadCacheNoUpdateTest() {
        when(cache.getUpdateDate()).thenReturn(LocalDateTime.now());
        assertFalse(cacheLoader.needsUpdate());
        cacheLoader.loadCache(extractor, reader);
        Mockito.verify(cacheLoader, times(1)).needsUpdate();
        Mockito.verify(cacheLoader, never()).refreshCache(any(), any());
        Mockito.verify(cacheLoader, never()).saveCache();
    }
}