package radar;

import data.MeasurmentValue;
import qualityIndex.AirQualityIndex;
import qualityIndex.ParamIndex;
import qualityIndex.ParamIndexLevel;

import java.util.Collections;

public class PolishRadarPrinter implements RadarPrinter {


    private static final String paramIndexFormat = "Parameter: %s, \n CalculationDate: %s, \n IndexLevelName: %s, \n SourceDataDate: %s, \n";
    private static final String currentMeasurmentValueFormat = "Station: %s, \n Parameter: %s, \n LatestMeasurmentDate: %s, \n MeasurmentValue: %s, \n";
    private static final String averageMeasurmentValueFormat = "Station: %s, \n Parameter: %s, \n FromDate: %s, \n ToDate: %s, \n AverageMeasurmentValue: %s, \n";
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
    public void printCurrentMeasurment(String stationName, String paramName, MeasurmentValue latestMeasurment) {
        StringBuilder result = new StringBuilder();
        result.append(separator);
        result.append(String.format(currentMeasurmentValueFormat, stationName, paramName, latestMeasurment.getDate(), latestMeasurment.getValue()));
        result.append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printAverageMeasurment(String stationName, String paramName, String fromDate, String toDate, double averageMeasurment) {
        StringBuilder result = new StringBuilder();
        result.append(separator);
        result.append(String.format(averageMeasurmentValueFormat, stationName, paramName, fromDate, toDate, (averageMeasurment == -1) ? null : averageMeasurment));
        result.append(separator);
        System.out.println(result.toString());
    }
}
