package data.qualityIndex;

import java.util.HashMap;
import java.util.Map;

public class ParamIndex {
    private String paramName;
    private String calcDate;
    private Object paramIndexLevel;
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

    public Object getParamIndexLevel() {
        return paramIndexLevel;
    }

    public void setParamIndexLevel(Object paramIndexLevel) {
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

