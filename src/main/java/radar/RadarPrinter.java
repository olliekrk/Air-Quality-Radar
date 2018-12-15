package radar;

import data.MeasurementValue;
import data.Param;
import data.Sensor;
import data.Station;
import data.qualityIndex.AirQualityIndex;

public interface RadarPrinter {
    void printIndexData(AirQualityIndex index);

    void printCurrentMeasurement(String stationName, String paramName, MeasurementValue latestMeasurment);

    void printAverageMeasurement(String stationName, String paramName, String fromDate, String toDate, double averageMeasurment);

    void printMaxAmplitudeParameter(String stationName, String fromDate, Sensor sensor, MeasurementValue maxValue, MeasurementValue minValue);

    void printMinMeasurementParameter(String stationName, MeasurementValue minimalValue, Param minimalParam, String date);

    void printNSensorsWithMaxParamValueForDay(int N, String paramName, String day, Station[] stations, Sensor[] sensors, MeasurementValue[] maxValues);

    void printParamExtremeValues(Sensor minSensor, MeasurementValue minValue, Sensor maxSensor, MeasurementValue maxValue);
}
