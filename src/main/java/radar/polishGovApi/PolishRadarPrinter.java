package radar.polishGovApi;

import data.MeasurementValue;
import data.Param;
import data.Sensor;
import data.qualityIndex.AirQualityIndex;
import data.qualityIndex.ParamIndex;
import data.qualityIndex.ParamIndexLevel;
import radar.RadarPrinter;

import java.util.Collections;

public class PolishRadarPrinter implements RadarPrinter {
    //this methods are actually universal and they work not only with Polish Gov API
    //rename this just to DefaultRadarPrinter?

    private static final String paramIndexFormat = "Parameter: %s, \n     CalculationDate: %s, \n     IndexLevelName: %s, \n     SourceDataDate: %s, \n";
    private static final String currentMeasurementValueFormat = "Station: %s, \n     Parameter: %s, \n     LatestMeasurmentDate: %s, \n     MeasurementValue: %s, \n";
    private static final String averageMeasurementValueFormat = "Station: %s, \n     Parameter: %s, \n     FromDate: %s, \n     ToDate: %s, \n     AverageMeasurmentValue: %s, \n";
    private static final String singleMeasurementFormat = "     MeasurmentDate: %s, \n     MeasurementValue: %s, \n";
    private static final String measurementInfoFormat = "Station: %s, \n     Parameter: %s, \n     Date: %s, \n";
    private static final String separator = String.join("", Collections.nCopies(40, "-")) + '\n';

    @Override
    public void printIndexData(AirQualityIndex index) {
        StringBuilder result = new StringBuilder();
        result.append("Id: ").append(index.getId()).append("\n").append(separator);
        ParamIndex[] params = (ParamIndex[]) index.getParamIndex();
        for (ParamIndex param : params) {
            ParamIndexLevel paramLevel = (ParamIndexLevel) param.getParamIndexLevel();
            String info = String.format(paramIndexFormat, param.getParamName(), param.getCalcDate(), (paramLevel == null) ? null : paramLevel.getIndexLevelName(), param.getSourceDataDate());
            result.append(info);
            result.append(separator);
        }
        System.out.println(result.toString());
    }

    @Override
    public void printCurrentMeasurement(String stationName, String paramName, MeasurementValue latestMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter current measurement info. \n")
                .append(String.format(currentMeasurementValueFormat, stationName, paramName, latestMeasurement.getDate(), latestMeasurement.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printAverageMeasurement(String stationName, String paramName, String fromDate, String toDate, double averageMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter average measurement value info. \n")
                .append(String.format(averageMeasurementValueFormat, stationName, paramName, fromDate, toDate, (averageMeasurement == -1) ? null : averageMeasurement))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printMaxAmplitudeParameter(String stationName, String fromDate, Sensor sensor, MeasurementValue maxValue, MeasurementValue minValue) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter with maximum amplitude info. \n")
                .append(String.format(measurementInfoFormat, stationName, sensor.getParam().getParamName(), fromDate))
                .append(separator)
                .append("Maximum: \n")
                .append(String.format(singleMeasurementFormat, maxValue.getDate(), maxValue.getValue()))
                .append(separator)
                .append("Minimum: \n")
                .append(String.format(singleMeasurementFormat, minValue.getDate(), minValue.getValue()))
                .append(separator);

        System.out.println(result.toString());
    }

    @Override
    public void printMinMeasurementParameter(String stationName, MeasurementValue minimalValue, Param minimalParam, String date) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter with minimal measurement value for given station and day info. \n")
                .append(String.format(measurementInfoFormat, stationName, minimalParam.getParamName(), date))
                .append(separator)
                .append("Minimal measured value: \n")
                .append(String.format(singleMeasurementFormat, minimalValue.getDate(), minimalValue.getValue()))
                .append(separator);

        System.out.println(result.toString());
    }
}
