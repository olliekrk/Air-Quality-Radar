package radar.GIOS;

import data.*;
import exceptions.MissingDataException;
import exceptions.UnknownParameterException;
import radar.DataAnalyzer;
import radar.RadarPrinter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static radar.DataAnalyzer.fromDateTime;

/**
 * Class implementing {@link RadarPrinter} interface
 * which is fully compatible with GIOś API.
 * Part of "Strategy" design pattern.
 */
public class RadarPrinterGIOS implements RadarPrinter {

    private static final int graphSize = 30;
    private static final String graphChar = "@";
    private static final String graphFormat = "%20s : %-30s : %s : %s \n";

    private static final int tableWidth = 50;
    private static final String bigSeparator = String.join("", Collections.nCopies(tableWidth, "=")) + '\n';
    private static final String separator = String.join("", Collections.nCopies(tableWidth, "~")) + '\n';
    private static final String indexParamFormat = "Parameter: %s, \n       CalculationDate: %s, \n       IndexLevelName: %s, \n       SourceDataDate: %s, \n";
    private static final String stationFormat = "StationName: %s, \nStationId: %s, \n";
    private static final String sensorFormat = "       SensorParam: %s, \n       SensorId: %s, \n";
    private static final String defaultMeasurementInfoFormat = "       MeasurementDate: %s, \n       MeasurementValue: %s, \n";
    private static final String twoDatesFormat = "       SinceDate: %s, \n       UntilDate: %s, \n";
    private static final String oneDateFormat = "       Date: %s, \n";

    @Override
    public void printIndexData(Station station, AirQualityIndex index) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Air quality index for given station info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(separator);

        ParamIndex[] params = index.getParamIndex();
        for (ParamIndex param : params) {
            ParamIndexLevel paramLevel = param.getParamIndexLevel();
            result.append(String.format(indexParamFormat, param.getParamName(), param.getCalcDate(), (paramLevel == null) ? null : paramLevel.getIndexLevelName(), param.getSourceDataDate()));
            result.append(separator);
        }
        System.out.println(result.toString());
    }

    @Override
    public void printMeasurement(Station station, Sensor sensor, MeasurementValue measurement) {
        String result = bigSeparator +
                "Parameter current measurement info. \n" +
                bigSeparator +
                String.format(stationFormat, station.getStationName(), station.getId()) +
                String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()) +
                separator +
                "Measurement: \n" +
                String.format(defaultMeasurementInfoFormat, measurement.getDate(), measurement.getValue()) +
                separator;
        System.out.println(result);
    }

    @Override
    public void printAverageMeasurement(Station station, Sensor sensor, LocalDateTime since, LocalDateTime until, double average) {
        String result = bigSeparator +
                "Parameter average measurement info. \n" +
                bigSeparator +
                String.format(stationFormat, station.getStationName(), station.getId()) +
                String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()) +
                String.format(twoDatesFormat, fromDateTime(since), fromDateTime(until)) +
                "Average measurement: \n" +
                String.format(defaultMeasurementInfoFormat, "(above)", (average == -1) ? null : average) +
                separator;
        System.out.println(result);
    }

    @Override
    public void printExtremeParamValuesSince(Station station, LocalDateTime since, Sensor sensor, MeasurementValue minValue, MeasurementValue maxValue) {
        String result = bigSeparator +
                "Parameter with maximum amplitude since given date info. \n" +
                bigSeparator +
                String.format(stationFormat, station.getStationName(), station.getId()) +
                String.format(oneDateFormat, fromDateTime(since)) +
                separator +
                String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()) +
                separator +
                "Maximum: \n" +
                String.format(defaultMeasurementInfoFormat, maxValue.getDate(), maxValue.getValue()) +
                separator +
                "Minimum: \n" +
                String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()) +
                separator;
        System.out.println(result);
    }

    @Override
    public void printParamMinimalValue(Station station, Sensor minSensor, MeasurementValue minValue, LocalDateTime date) {
        String result = bigSeparator +
                "Parameter with minimal measurement value for given station and date info. \n" +
                bigSeparator +
                String.format(stationFormat, station.getStationName(), station.getId()) +
                separator +
                "Minimal measured value: \n" +
                String.format(sensorFormat, minSensor.getParam().getParamName(), minSensor.getId()) +
                String.format(oneDateFormat, fromDateTime(date)) +
                String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()) +
                separator;
        System.out.println(result);
    }

    @Override
    public void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue) {
        String result = bigSeparator +
                "Minimum and maximum measurement values for given parameter info. \n" +
                bigSeparator +
                "Minimum: \n" +
                String.format(stationFormat, minStation.getStationName(), minStation.getId()) +
                String.format(sensorFormat, minSensor.getParam().getParamName(), minSensor.getId()) +
                String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()) +
                separator +
                "Maximum: \n" +
                String.format(stationFormat, maxStation.getStationName(), maxStation.getId()) +
                String.format(sensorFormat, maxSensor.getParam().getParamName(), maxSensor.getId()) +
                String.format(defaultMeasurementInfoFormat, maxValue.getDate(), maxValue.getValue());
        System.out.println(result);
    }

    @Override
    public void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, LocalDateTime date, String paramCode, int n) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append(n)
                .append(" sensors with maximum parameter value for given day info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(oneDateFormat, fromDateTime(date)))
                .append(separator);

        for (int i = 0; i < sortedSensors.length; i++) {
            result
                    .append("Top ")
                    .append(sortedSensors.length - i)
                    .append(": \n")
                    .append(String.format(sensorFormat, sortedSensors[i].getParam().getParamName(), sortedSensors[i].getId()))
                    .append(String.format(defaultMeasurementInfoFormat, sortedValues[i].getDate(), sortedValues[i].getValue()))
                    .append(separator);
        }
        System.out.println(result.toString());
    }

    @Override
    public void printGraph(Station station, Sensor sensor, MeasurementData data, LocalDateTime since, LocalDateTime until, ParamType paramType, double range) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Graph presenting parameter's measurement values in given period. \n")
                .append(bigSeparator);

        double oneCharValue = range / graphSize;
        for (LocalDateTime dateTime = since; dateTime.isBefore(until) || dateTime.isEqual(until); dateTime = dateTime.plusHours(1)) {
            String date = DataAnalyzer.fromDateTime(dateTime);
            MeasurementValue value;
            String graphChars;
            try {
                value = DataAnalyzer.getValue(data, dateTime, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.DEFAULT);
                int x = (int) Math.floor(value.getValue() / oneCharValue);
                graphChars = String.join("", Collections.nCopies(x + 1, graphChar));
            } catch (MissingDataException e) {
                value = null;
                graphChars = "";
            }
            result.append(String.format(graphFormat, date, station.getStationName(), graphChars, (value == null) ? "unknown" : value.getValue()));
        }
        System.out.println(result.toString());
    }

    @Override
    public void printCommonGraph(List<Station> stations, Map<Integer, Sensor> sensors, Map<Integer, MeasurementData> dataMap, LocalDateTime since, LocalDateTime until, ParamType paramType, Double range) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Common summary graph, for all stations. \n")
                .append(bigSeparator);

        double oneCharValue = range / graphSize;

        List<Station> completeDataStations = new ArrayList<>();
        for (Station station : stations) {
            if (sensors.containsKey(station.getId())) {
                Sensor sensor = sensors.get(station.getId());
                if (dataMap.containsKey(sensor.getId())) {
                    completeDataStations.add(station);
                }
            }
        }

        char letter = 'A';
        char letterUsed;
        int uniqueLettersCount = completeDataStations.size();
        int i = 0;

        for (LocalDateTime dateTime = since; dateTime.isBefore(until) || dateTime.isEqual(until); dateTime = dateTime.plusHours(1)) {
            String date = DataAnalyzer.fromDateTime(dateTime);
            MeasurementValue value;
            String chart;
            for (Station station : completeDataStations) {
                letterUsed = (char) (letter + i);
                i += 1;
                i %= uniqueLettersCount;
                try {

                    value = DataAnalyzer.getValue(dataMap.get(sensors.get(station.getId()).getId()), dateTime, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.DEFAULT);
                    int x = (int) Math.floor(value.getValue() / oneCharValue);
                    chart = String.join("", Collections.nCopies(x + 1, String.valueOf(letterUsed)));
                } catch (MissingDataException e) {
                    value = null;
                    chart = "";
                }
                result.append(String.format(graphFormat, date, station.getStationName(), chart, value == null ? "unknown" : value.getValue()));
            }
        }
        System.out.println(result.toString());
    }

    @Override
    public void printOverAcceptableLevel(Station station, LocalDateTime date, Sensor[] sensorsMax, MeasurementValue[] valuesMax, Double[] levelsMax) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append(sensorsMax.length)
                .append(" sensors on which acceptable parameter's measurement values were exceeded. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(oneDateFormat, fromDateTime(date)))
                .append(separator);

        for (int i = 0; i < sensorsMax.length; i++) {
            try {
                result
                        .append("Top ")
                        .append(sensorsMax.length - i)
                        .append(": \n")
                        .append(String.format(sensorFormat, sensorsMax[i].getParam().getParamName(), sensorsMax[i].getId()))
                        .append(String.format(defaultMeasurementInfoFormat, valuesMax[i].getDate(), valuesMax[i].getValue()))
                        .append("Acceptable value for parameter: ")
                        .append(ParamType.getParamType(sensorsMax[i].getParam().getParamCode()).getAcceptableLevel())
                        .append(" [ug / m^3] \n")
                        .append("Acceptable value was exceeded by: ")
                        .append(levelsMax[i] * 100 - 100)
                        .append("% \n")
                        .append(separator);
            } catch (UnknownParameterException e) {
                //unreachable
            }
        }
        System.out.println(result.toString());
    }
}
