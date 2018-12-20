package data;

import java.util.HashMap;
import java.util.Map;

public class AirQualityIndex {
    private Integer id;
    private Object[] paramIndex;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object[] getParamIndex() {
        return paramIndex;
    }

    public void setParamIndex(Object[] paramIndex) {
        this.paramIndex = paramIndex;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}