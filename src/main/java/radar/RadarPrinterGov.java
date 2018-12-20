package radar;

import data.*;

import java.util.Collections;

public class RadarPrinterGov implements RadarPrinter {

    private static final int tableWidth = 50;
    private static final String bigSeparator = String.join("", Collections.nCopies(tableWidth, "=")) + '\n';
    private static final String separator = String.join("", Collections.nCopies(tableWidth, "~")) + '\n';
    private static final String indexParamFormat = "Parameter: %s, \n       CalculationDate: %s, \n       IndexLevelName: %s, \n       SourceDataDate: %s, \n";
    private static final String stationFormat = "StationName: %s, \nStationId: %s, \n";
    private static final String sensorFormat = "       SensorParam: %s, \n       SensorId: %s, \n";
    private static final String defaultMeasurementInfoFormat = "       MeasurementDate: %s, \n       MeasurementValue: %s, \n";
    private static final String twoDatesFormat = "       FromDate: %s, \n       ToDate: %s, \n";
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
    public void printCurrentMeasurement(Station station, Sensor sensor, MeasurementValue latestMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Parameter current measurement info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()))
                .append("Latest measurement: \n")
                .append(String.format(defaultMeasurementInfoFormat, latestMeasurement.getDate(), latestMeasurement.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printAverageMeasurement(Station station, Sensor sensor, String fromDate, String toDate, double averageMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Parameter average measurement info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()))
                .append(String.format(twoDatesFormat, fromDate, toDate))
                .append("Average measurement: \n")
                .append(String.format(defaultMeasurementInfoFormat, "(above)", (averageMeasurement == -1) ? null : averageMeasurement))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printExtremeParamValuesPeriod(Station station, String fromDate, String toDate, Sensor sensor, MeasurementValue currentMin, MeasurementValue currentMax) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Parameter with maximum amplitude info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(twoDatesFormat, fromDate, toDate))
                .append(separator)
                .append(String.format(sensorFormat, sensor.getParam().getParamName(), sensor.getId()))
                .append(separator)
                .append("Maximum: \n")
                .append(String.format(defaultMeasurementInfoFormat, currentMax.getDate(), currentMax.getValue()))
                .append(separator)
                .append("Minimum: \n")
                .append(String.format(defaultMeasurementInfoFormat, currentMin.getDate(), currentMin.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printParamMinimalValue(Station station, Sensor currentSensor, MeasurementValue currentMin, String date) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Parameter with minimal measurement value for given station and day info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(separator)
                .append("Minimal measured value: \n")
                .append(String.format(sensorFormat, currentSensor.getParam().getParamName(), currentSensor.getId()))
                .append(String.format(oneDateFormat, date))
                .append(String.format(defaultMeasurementInfoFormat, currentMin.getDate(), currentMin.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printExtremeParamValuesWhereAndWhen(String paramName, Station minStation, Sensor minSensor, MeasurementValue minValue, Station maxStation, Sensor maxSensor, MeasurementValue maxValue) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append("Minimum and maximum measurement values for given parameter info. \n")
                .append(bigSeparator)

                .append("Minimum: \n")
                .append(String.format(stationFormat, minStation.getStationName(), minStation.getId()))
                .append(String.format(sensorFormat, minSensor.getParam().getParamName(), minSensor.getId()))
                .append(String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()))

                .append(separator)
                .append("Maximum: \n")
                .append(String.format(stationFormat, maxStation.getStationName(), maxStation.getId()))
                .append(String.format(sensorFormat, maxSensor.getParam().getParamName(), maxSensor.getId()))
                .append(String.format(defaultMeasurementInfoFormat, maxValue.getDate(), maxValue.getValue()));

        System.out.println(result.toString());
    }

    @Override
    public void printNSensors(Station station, Sensor[] sortedSensors, MeasurementValue[] sortedValues, String dateHour, String paramCode, int n) {
        StringBuilder result = new StringBuilder();
        result
                .append(bigSeparator)
                .append(n + " sensors with maximum parameter value for given day info. \n")
                .append(bigSeparator)
                .append(String.format(stationFormat, station.getStationName(), station.getId()))
                .append(String.format(oneDateFormat, dateHour))
                .append(separator);

        for (int i = 0; i < sortedSensors.length; i++) {
            result
                    .append("Top " + (sortedSensors.length - i) + ": \n")
                    .append(String.format(sensorFormat, sortedSensors[i].getParam().getParamName(), sortedSensors[i].getId()))
                    .append(String.format(defaultMeasurementInfoFormat, sortedValues[i].getDate(), sortedValues[i].getValue()))
                    .append(separator);
        }
        System.out.println(result.toString());
    }
}
