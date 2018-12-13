package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurmentData {

    private String key;
    private List<MeasurmentValue> values = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MeasurmentValue> getValues() {
        return values;
    }

    public void setValues(List<MeasurmentValue> values) {
        this.values = values;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}