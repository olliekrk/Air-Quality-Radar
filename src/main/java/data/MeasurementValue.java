package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Data container class for storing parsed JSON measurement value information.
 */
public class MeasurementValue {

    private String date;
    private Double value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}