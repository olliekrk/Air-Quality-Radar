package radar.cache;

import data.*;

public interface RadarPrinter {
    void printIndexData(Station station, AirQualityIndex index);

    void printCurrentMeasurement(String stationName, String paramName, MeasurementValue latestMeasurment);

    void printAverageMeasurement(String stationName, String paramName, String fromDate, String toDate, double averageMeasurment);

    void printMaxAmplitudeParameter(String stationName, String fromDate, Sensor sensor, MeasurementValue maxValue, MeasurementValue minValue);

    void printMinMeasurementParameter(String stationName, MeasurementValue minimalValue, Param minimalParam, String date);

    void printNSensorsWithMaxParamValueForDay(int N, String paramName, String day, Station[] stations, Sensor[] sensors, MeasurementValue[] maxValues);

    void printParamExtremeValues(Sensor minSensor, MeasurementValue minValue, Sensor maxSensor, MeasurementValue maxValue);

    //
    void printExtremeParamValuesPeriod(Station station, String fromDate, String toDate, Sensor currentSensor, MeasurementValue currentMin, MeasurementValue currentMax);

    void printParamMinimalValue(Station station, Sensor currentSensor, MeasurementValue currentMin, String date);

    void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue);

    void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, String dateHour, String paramCode, int n);
}
