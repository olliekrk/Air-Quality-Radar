package radar;

import data.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Class used to display results of Air Quality Radar computations by printing them to the console in form of tables.
 */
public interface RadarPrinter {
    /**
     * Prints information about air quality index of given station.
     *
     * @param station station which air quality index will be printed
     * @param index   air quality index to be printed
     */
    void printIndexData(Station station, AirQualityIndex index);

    /**
     * Prints information about measurement value of specific station's sensor.
     *
     * @param station     station on which that value was measured
     * @param sensor      sensor which measured that value
     * @param measurement value to be printed
     */
    void printMeasurement(Station station, Sensor sensor, MeasurementValue measurement);

    /**
     * Prints information about average measurement values of specified station's sensor and period of time.
     *
     * @param station station on which values were measured
     * @param sensor  sensor which measured these values
     * @param since   measurement date since when values should be included in average
     * @param until   measurement date until values should be included in average
     * @param average average measurement value in given period of time
     */
    void printAverageMeasurement(Station station, Sensor sensor, LocalDateTime since, LocalDateTime until, double average);

    /**
     * Prints information about highest and lowest measured values since given date.
     *
     * @param station  station on which those values were measured
     * @param since    date since
     * @param sensor   sensor which measured those values
     * @param minValue lowest value measured since given date
     * @param maxValue highest value measured since given date
     */
    void printExtremeParamValuesSince(Station station, LocalDateTime since, Sensor sensor, MeasurementValue minValue, MeasurementValue maxValue);

    /**
     * Prints information about which parameter had lowest measurement values on given station since given date.
     *
     * @param station   station on which those values were measured
     * @param minSensor sensor which measured minimal value
     * @param minValue  minimal measured value
     * @param date      date since
     */
    void printParamMinimalValue(Station station, Sensor minSensor, MeasurementValue minValue, LocalDateTime date);

    /**
     * Prints information about where and when were measured highest given parameter's values.
     *
     * @param paramName  code of the parameter
     * @param minStation station on which minimal value was measured
     * @param minSensor  sensor which measured minimal value
     * @param minValue   minimal value
     * @param maxStation station on which maximum value was measured
     * @param maxSensor  sensor which measured maximum value
     * @param maxValue   maximum value
     */
    void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue);

    /**
     * Prints up to N sensors of given station which measured the highest values of given parameter since given date.
     *
     * @param station       station on which those values were measured
     * @param sortedSensors array of sensors sorted by measurement values in ascending order
     * @param sortedValues  array of measurement values sorted in ascending order
     * @param date          date since
     * @param paramCode     code of the parameter
     * @param n             number of sensors
     */
    void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, LocalDateTime date, String paramCode, int n);

    /**
     * Prints bar chart showing how measurement values were changing every hour on given stations between two dates.
     *
     * @param station   station on which values were measured
     * @param sensor    sensor which measured those values
     * @param data      measurement data containing all data collected by sensor
     * @param since     date since
     * @param until     date until
     * @param paramType type of parameter
     * @param range     the absolute difference between maximum and minimum measured value
     */
    void printGraph(Station station, Sensor sensor, MeasurementData data, LocalDateTime since, LocalDateTime until, ParamType paramType, double range);

    /**
     * Prints bar chart showing how measurement values were changing every hour on given stations between two dates.
     *
     * @param stations  stations on which values were measured
     * @param sensors   sensors which measured those values
     * @param dataMap   measurement data map containing all data collected by sensor
     * @param since     date since
     * @param until     date until
     * @param paramType type of parameter
     * @param range     the absolute difference between maximum and minimum measured value
     */
    void printCommonGraph(List<Station> stations, Map<Integer, Sensor> sensors, Map<Integer, MeasurementData> dataMap, LocalDateTime since, LocalDateTime until, ParamType paramType, Double range);

    /**
     * Prints information about station's sensors which reached measurement values over acceptable parameter's level in given date.
     *
     * @param station    station on which sensors were checked
     * @param date       date when acceptable measurement values were reached
     * @param sensorsMax array of sensors sorted ascending
     * @param valuesMax  array of measurement values of those sensors
     * @param levelsMax  array telling how much in % were those levels exceeded
     * @see ParamType
     */
    void printOverAcceptableLevel(Station station, LocalDateTime date, Sensor[] sensorsMax, MeasurementValue[] valuesMax, Double[] levelsMax);
}
