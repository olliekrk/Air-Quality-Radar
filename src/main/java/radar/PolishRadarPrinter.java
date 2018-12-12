package radar;

import qualityIndex.AirQualityIndex;
import qualityIndex.ParamIndex;
import qualityIndex.ParamIndexLevel;

import java.util.Collections;

public class PolishRadarPrinter implements RadarPrinter {

    private static final String paramIndexFormat = "Parameter: %s" + '\n' + "CalculationDate: %s," + '\n' + "IndexLevelName: %s, " + '\n' + "SourceDataDate: %s" + '\n';
    private static final String separator = String.join("", Collections.nCopies(40, "-"));

    @Override
    public void printIndexData(AirQualityIndex index) {
        StringBuilder result = new StringBuilder();
        result.append("INDEX's ID: ").append(index.getId()).append("\n");
        ParamIndex[] params = (ParamIndex[]) index.getParamIndex();
        for (ParamIndex param : params) {
            ParamIndexLevel paramLevel = (ParamIndexLevel) param.getParamIndexLevel();
            String info = String.format(paramIndexFormat, param.getParamName(), param.getCalcDate(), paramLevel.getIndexLevelName(), param.getSourceDataDate());
            result.append(info);
            result.append(separator);
        }
        System.out.println(result.toString());
    }
}
