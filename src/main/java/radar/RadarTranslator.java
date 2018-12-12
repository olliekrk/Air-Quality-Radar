package radar;

import qualityIndex.AirQualityIndex;

import java.io.InputStream;

public interface RadarTranslator {
    AirQualityIndex readIndexData(InputStream input);
}
