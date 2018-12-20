package radar;

import data.*;

public interface RadarPrinter {
    void printIndexData(Station station, AirQualityIndex index);

    void printCurrentMeasurement(Station stationName, Sensor paramName, MeasurementValue latestMeasurment);

    void printAverageMeasurement(Station station, Sensor sensor, String fromDate, String toDate, double averageMeasurment);

    //
    void printExtremeParamValuesPeriod(Station station, String fromDate, String toDate, Sensor currentSensor, MeasurementValue currentMin, MeasurementValue currentMax);

    void printParamMinimalValue(Station station, Sensor currentSensor, MeasurementValue currentMin, String date);

    void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue);

    void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, String dateHour, String paramCode, int n);
}
