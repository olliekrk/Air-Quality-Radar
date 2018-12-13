package radar;

import data.MeasurmentValue;
import qualityIndex.AirQualityIndex;

public interface RadarPrinter {
    void printIndexData(AirQualityIndex index);

    void printCurrentMeasurment(String stationName, String paramName, MeasurmentValue latestMeasurment);

    void printAverageMeasurment(String stationName, String paramName, String fromDate, String toDate, double averageMeasurment);
}
