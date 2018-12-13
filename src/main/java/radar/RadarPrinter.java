package radar;

import data.MeasurmentData;
import data.MeasurmentValue;
import qualityIndex.AirQualityIndex;

public interface RadarPrinter {
    void printIndexData(AirQualityIndex index);

    void printCurrentMeasurment(String stationName, String paramName, MeasurmentValue latestMeasurment);
}
