package data;

import java.util.HashMap;
import java.util.Map;

public class ParamIndexLevel {
    private String id;
    private String indexLevelName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ParamIndexLevel(String id, String indexLevelName) {
        this.id = id;
        this.indexLevelName = indexLevelName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexLevelName() {
        return indexLevelName;
    }

    public void setIndexLevelName(String indexLevelName) {
        this.indexLevelName = indexLevelName;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
