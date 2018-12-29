package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Data container class for storing parsed JSON air quality index information.
 */
public class AirQualityIndex {
    private Integer id;
    private ParamIndex[] paramIndex;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ParamIndex[] getParamIndex() {
        return paramIndex;
    }

    public void setParamIndex(ParamIndex[] paramIndex) {
        this.paramIndex = paramIndex;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}