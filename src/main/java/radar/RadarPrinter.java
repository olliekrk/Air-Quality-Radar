package radar;

import data.*;

import java.time.LocalDateTime;

public interface RadarPrinter {
    void printIndexData(Station station, AirQualityIndex index);

    void printMeasurement(Station stationName, Sensor paramName, MeasurementValue measurement);

    void printAverageMeasurement(Station station, Sensor sensor, LocalDateTime since, LocalDateTime until, double average);

    void printExtremeParamValuesSince(Station station, LocalDateTime since, Sensor sensor, MeasurementValue minValue, MeasurementValue maxValue);

    void printParamMinimalValue(Station station, Sensor minSensor, MeasurementValue minValue, LocalDateTime date);

    void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue);

    void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, LocalDateTime date, String paramCode, int n);

    void printGraph(Station station, Sensor sensor, MeasurementData data, LocalDateTime since, LocalDateTime until, ParamType paramType, double range);
}
