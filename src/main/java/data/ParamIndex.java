package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Data container class for storing parsed JSON air quality parameter's index information.
 */
public class ParamIndex {
    private String paramName;
    private String calcDate;
    private ParamIndexLevel paramIndexLevel;
    private String sourceDataDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getCalcDate() {
        return calcDate;
    }

    public void setCalcDate(String calcDate) {
        this.calcDate = calcDate;
    }

    public ParamIndexLevel getParamIndexLevel() {
        return paramIndexLevel;
    }

    public void setParamIndexLevel(ParamIndexLevel paramIndexLevel) {
        this.paramIndexLevel = paramIndexLevel;
    }

    public String getSourceDataDate() {
        return sourceDataDate;
    }

    public void setSourceDataDate(String sourceDataDate) {
        this.sourceDataDate = sourceDataDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}

