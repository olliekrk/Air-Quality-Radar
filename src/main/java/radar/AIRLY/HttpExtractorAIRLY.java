package radar.AIRLY;

import radar.HttpExtractor;

/**
 * Class implementing {@link HttpExtractor} interface, which methods
 * work typically with Airly API.
 * Part of "Strategy" design pattern.
 */
public class HttpExtractorAIRLY implements HttpExtractor {
    @Override
    public String extractAllStationsData() {
        return null;
    }

    @Override
    public String extractAllSensorsData(Integer stationId) {
        return null;
    }

    @Override
    public String extractMeasurementData(Integer sensorId) {
        return null;
    }

    @Override
    public String extractIndexData(Integer stationId) {
        return null;
    }

    @Override
    public String connectAndExtract(String requestURL) {
        return null;
    }
}
