package radar;

import data.MeasurementValue;
import data.Sensor;
import data.qualityIndex.AirQualityIndex;

public interface RadarPrinter {
    void printIndexData(AirQualityIndex index);

    void printCurrentMeasurement(String stationName, String paramName, MeasurementValue latestMeasurment);

    void printAverageMeasurement(String stationName, String paramName, String fromDate, String toDate, double averageMeasurment);

    void printMaxAmplitudeParameter(String stationName, String fromDate, Sensor sensor, MeasurementValue maxValue, MeasurementValue minValue);
}
