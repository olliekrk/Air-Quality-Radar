package radar;

import qualityIndex.AirQualityIndex;

import java.io.IOException;
import java.io.InputStream;

public interface RadarTranslator {
    AirQualityIndex readIndexData(String data) throws IOException;
}
