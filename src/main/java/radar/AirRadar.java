package radar;

import data.*;
import exceptions.MissingDataException;
import exceptions.UnknownParameterException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main air quality radar application entry-class providing interface to proceed JSON data extracted
 * from air quality APIs.
 * Main part of "Facade" design pattern as application user may call only methods of this class directly.
 * Part of "Strategy" design patterns, as {@link HttpExtractor}, {@link RadarPrinter} and {@link RadarReader}
 * can be implemented independently from each other, still maintaining behaviour of this class.
 * Also part of "Factory" design pattern.
 */
public abstract class AirRadar {

    //all 4 are parts of separate "Strategy" design patterns
    /**
     * used to extract data from HTTP service
     */
    protected HttpExtractor extractor;
    /**
     * used to print data to the console
     */
    protected RadarPrinter printer;
    /**
     * used to convert JSON data into POJO(s)
     */
    protected RadarReader reader;
    /**
     * used to extract data from local cache file
     */
    protected CacheSeeker seeker;

    /**
     * Finds and prints information about current air quality index for station of given name,
     * contained by {@link AirQualityIndex} and extracted from local cache.
     *
     * @param stationName name of station for which data will be analyzed
     * @throws MissingDataException when station's name has not been recognized
     *                              or when cache does not contain data for given station name
     */
    public void getAirQualityIndexForStation(String stationName) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        AirQualityIndex index = seeker.findIndex(station.getId());
        printer.printIndexData(station, index);
    }

    /**
     * Finds and prints information about parameter's measurement value for given station, date and time.
     *
     * @param stationName name of station for which data will be analyzed
     * @param paramCode   parameter for which measurement data will be analyzed
     * @param date        date and time for which measurement data will be analyzed
     * @throws MissingDataException      when there is no sufficient information available in local cache
     *                                   or when cache does not contain data for given station name
     * @throws UnknownParameterException when given parameter is not recognized as any supported parameter
     */
    public void getParamValueForStation(String stationName, String paramCode, LocalDateTime date) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
        MeasurementData data = seeker.findData(sensor.getId());

        MeasurementValue resultValue = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.DEFAULT);
        printer.printMeasurement(station, sensor, resultValue);
    }

    /**
     * Finds and prints information about parameter's average measurement value for given station and period of time.
     *
     * @param stationName name of station for which data will be analyzed
     * @param paramCode   parameter for which measurement data will be analyzed
     * @param since       date and time since when measurement data should be analyzed
     * @param until       date and time until when measurement data should be analyzed
     * @throws MissingDataException      when there is no sufficient information available in local cache
     *                                   or when cache does not contain data for given station name
     * @throws UnknownParameterException when given parameter is not recognized as any supported parameter
     */
    public void getAverageParamValuePeriod(String stationName, String paramCode, LocalDateTime since, LocalDateTime until) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
        MeasurementData data = seeker.findData(sensor.getId());

        MeasurementValue resultValue = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.AVERAGE);
        printer.printAverageMeasurement(station, sensor, since, until, resultValue.getValue());
    }

    /**
     * Finds and prints information about parameter's extreme measurement values for given station and since given date.
     *
     * @param stationName name of station for which data will be analyzed
     * @param since       date and time since when measurement data should be analyzed
     * @throws MissingDataException when there is no sufficient information available in local cache
     *                              or when cache does not contain data for given station name
     */
    public void getExtremeParamValuePeriod(String stationName, LocalDateTime since) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue currentMin = null;
        MeasurementValue currentMax = null;
        Sensor currentSensor = null;
        Double currentAmplitude = (double) -1;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());

            MeasurementValue maybeMin = DataAnalyzer.getValue(data, since, null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MIN);
            MeasurementValue maybeMax = DataAnalyzer.getValue(data, since, null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MAX);
            Double amplitude = maybeMax.getValue() - maybeMin.getValue();

            if (amplitude > currentAmplitude) {
                currentSensor = sensor;
                currentMin = maybeMin;
                currentMax = maybeMax;
                currentAmplitude = amplitude;
            }
        }
        printer.printExtremeParamValuesSince(station, since, currentSensor, currentMin, currentMax);
    }

    /**
     * Finds and prints information about parameter which had the lowest measurement value on given station and in given date.
     *
     * @param stationName name of station for which data will be analyzed
     * @param date        date and time for which measurement data should be analyzed
     * @throws MissingDataException when there is no sufficient information available in local cache
     *                              or when cache does not contain data for given station name
     */
    public void getParamOfMinimalValue(String stationName, LocalDateTime date) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue minValue = null;
        Sensor minSensor = null;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue maybeMin = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.MIN);
            if (maybeMin != null && maybeMin.getValue() != null && (minValue == null || minValue.getValue() > maybeMin.getValue())) {
                minValue = maybeMin;
                minSensor = sensor;
            }
        }
        printer.printParamMinimalValue(station, minSensor, minValue, date);
    }

    /**
     * Finds and prints N sensor of given station, sorted ascending, which measured highest parameter's value in given date.
     *
     * @param stationName name of station for which data will be analyzed
     * @param paramCode   parameter for which measurement data will be analyzed
     * @param date        date and time for which measurement data should be analyzed
     * @param n           maximum number of sensors that should be printed
     * @throws MissingDataException      when there is no sufficient information available in local cache
     *                                   or when cache does not contain data for given station name
     * @throws UnknownParameterException when given parameter is not recognized as any supported parameter
     */
    public void getNSensorsWithMaximumParamValue(String stationName, String paramCode, LocalDateTime date, int n) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);

        List<Sensor> sensors = seeker
                .findStationSensors(station.getId())
                .stream()
                .filter(x -> x.getParam().getParamFormula().compareToIgnoreCase(paramType.getParamFormula()) == 0)
                .collect(Collectors.toList());

        class TmpClass implements Comparable {
            private Sensor sensor;
            private MeasurementValue value;

            private TmpClass(Sensor sensor, MeasurementValue value) {
                this.sensor = sensor;
                this.value = value;
            }

            @Override
            public int compareTo(Object o) {
                TmpClass other = (TmpClass) o;
                if (this.value == null) return 1;
                if (other.value == null) return -1;
                if (this.value == other.value) return 0;
                return this.value.getValue() < other.value.getValue() ? -1 : 1;
            }
        }

        List<TmpClass> tmpClassList = new ArrayList<>();

        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue maxValue = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.MAX);
            tmpClassList.add(new TmpClass(sensor, maxValue));
        }

        TmpClass[] sortedTmp = tmpClassList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> x.value != null)
                .sorted()
                .toArray(TmpClass[]::new);

        if (sortedTmp.length > n) {
            sortedTmp = Arrays.copyOfRange(sortedTmp, sortedTmp.length - n, sortedTmp.length);
        }

        Sensor[] sortedSensors = new Sensor[sortedTmp.length];
        MeasurementValue[] sortedValues = new MeasurementValue[sortedTmp.length];
        for (int i = 0; i < sortedTmp.length; i++) {
            sortedSensors[i] = sortedTmp[i].sensor;
            sortedValues[i] = sortedTmp[i].value;
        }
        printer.printNSensors(station, sortedSensors, sortedValues, date, paramCode, n);
    }

    /**
     * Finds and prints information about when and where were measured extreme given parameter's values.
     *
     * @param paramCode parameter for which measurement data will be analyzed
     * @throws MissingDataException      when there is no sufficient information available in local cache
     * @throws UnknownParameterException when given parameter is not recognized as any supported parameter
     */
    public void getExtremeParamValueWhereAndWhen(String paramCode) throws UnknownParameterException, MissingDataException {
        Station minStation = null, maxStation = null;
        Sensor minSensor = null, maxSensor = null;
        MeasurementValue minValue = null, maxValue = null;
        Map<String, Station> allStationsMap = seeker.getCache().getAllStations();
        ParamType paramType = ParamType.getParamType(paramCode);

        for (Station station : allStationsMap.values()) {
            try {
                Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
                MeasurementData data = seeker.findData(sensor.getId());
                MeasurementValue maybeMin = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MIN);
                MeasurementValue maybeMax = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MAX);
                if (maybeMin != null && maybeMin.getValue() != null && (minValue == null || minValue.getValue() > maybeMin.getValue())) {
                    minSensor = sensor;
                    minValue = maybeMin;
                    minStation = station;
                }
                if (maybeMax != null && maybeMax.getValue() != null && (maxValue == null || maxValue.getValue() < maybeMax.getValue())) {
                    maxSensor = sensor;
                    maxValue = maybeMax;
                    maxStation = station;
                }
            } catch (MissingDataException e) {
                //just continue searching
            }
        }
        if (minStation == null || maxStation == null || minValue.getValue() == null || maxValue.getValue() == null)
            throw new MissingDataException("No sufficient data is available for parameter: " + paramType.getParamFormula());
        printer.printExtremeParamValuesWhereAndWhen(paramCode, minStation, minSensor, minValue, maxStation, maxSensor, maxValue);
    }

    /**
     * Finds and prints bar chart in console displaying how measurement values changed every hour for every station.
     *
     * @param stationNames names of stations for which data will be analyzed
     * @param paramCode    parameter for which measurement data will be analyzed
     * @param since        date and time since when measurement data should be analyzed (starting hour)
     * @param until        date and time until when measurement data should be analyzed (ending hour)
     * @throws MissingDataException      when there is no sufficient information available in local cache
     * @throws UnknownParameterException when given parameter is not recognized as any supported parameter
     */
    public void drawGraph(String[] stationNames, String paramCode, LocalDateTime since, LocalDateTime until) throws UnknownParameterException, MissingDataException {
        ParamType paramType = ParamType.getParamType(paramCode);

        List<Station> stations = new ArrayList<>();
        for (String name : stationNames) {
            try {
                Station station = seeker.findStation(name);
                stations.add(station);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add to list
            }
        }
        //key is stationId
        Map<Integer, Sensor> sensors = new HashMap<>();
        for (Station station : stations) {
            try {
                Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
                sensors.put(station.getId(), sensor);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add
            }
        }
        //key is sensorId
        Map<Integer, MeasurementData> dataMap = new HashMap<>();
        for (Sensor sensor : sensors.values()) {
            try {
                MeasurementData data = seeker.findData(sensor.getId());
                dataMap.put(sensor.getId(), data);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add
            }
        }

        MeasurementValue maxValue = null;
        MeasurementValue minValue = null;

        //finding min and max measured values
        for (MeasurementData data : dataMap.values()) {
            try {
                MeasurementValue maybeMax = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MAX);
                MeasurementValue maybeMin = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MIN);
                if (maxValue == null || maxValue.getValue() < maybeMax.getValue()) {
                    maxValue = maybeMax;
                }
                if (minValue == null || minValue.getValue() > maybeMin.getValue()) {
                    minValue = maybeMin;
                }
            } catch (MissingDataException e) {
                //just do not compare
            }
        }

        if (maxValue == null || minValue == null)
            throw new MissingDataException("No sufficient data is available to draw a graph.");

        //drawing graph
        for (Station station : stations) {
            if (sensors.containsKey(station.getId())) {
                Sensor sensor = sensors.get(station.getId());
                if (dataMap.containsKey(sensor.getId())) {
                    MeasurementData data = dataMap.get(sensor.getId());
                    printer.printGraph(station, sensor, data, since, until, paramType, maxValue.getValue());
                }
            }
        }

        //alternative way of drawing graph
        printer.printCommonGraph(stations, sensors, dataMap, since, until, paramType, maxValue.getValue());
    }
}
