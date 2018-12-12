package radar;

import qualityIndex.AirQualityIndex;

import java.io.InputStream;

public class PolishRadarTranslator implements RadarTranslator {
    @Override
    public AirQualityIndex readIndexData(InputStream input) {
        //TODO: api i parsować JSON na POJO
        return null;
    }
}
