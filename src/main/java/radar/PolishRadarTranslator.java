package radar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import qualityIndex.AirQualityIndex;
import qualityIndex.ParamIndex;
import qualityIndex.ParamIndexLevel;

import java.io.IOException;

public class PolishRadarTranslator implements RadarTranslator {

    private static final int paramsCount = 8;
    private static final String[] params = new String[]{"st", "so2", "no2", "co", "pm10", "pm25", "o3", "c6h6"};

    @Override
    public AirQualityIndex readIndexData(String data) throws IOException {
        AirQualityIndex index = new AirQualityIndex();

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(data);
        if (jsonTree.isJsonObject()) {

            JsonObject indexObject = jsonTree.getAsJsonObject();
            String indexId = indexObject.get("id").getAsString();

            ParamIndex[] paramIndices = new ParamIndex[paramsCount];
            for (int i = 0; i < paramsCount; i++) {
                paramIndices[i] = new ParamIndex();

                JsonElement calcDateEl = indexObject.get(params[i].concat("CalcDate"));
                JsonElement sourceDataDateEl = indexObject.get(params[i].concat("SourceDataDate"));
                JsonElement indexElement = indexObject.get(params[i].concat("IndexLevel"));

                String calcDate = calcDateEl.isJsonNull() ? null : calcDateEl.getAsString();
                String sourceDataDate = sourceDataDateEl.isJsonNull() ? null : sourceDataDateEl.getAsString();
                ParamIndexLevel paramIndexLevel = null;

                if (!indexElement.isJsonNull()) {
                    JsonObject indexLevel = indexElement.getAsJsonObject();
                    String indexLevelId = indexLevel.get("id").getAsString();
                    String indexLevelName = indexLevel.get("indexLevelName").getAsString();
                    paramIndexLevel = new ParamIndexLevel(indexLevelId, indexLevelName);
                }
                paramIndices[i].setParamName(params[i]);
                paramIndices[i].setCalcDate(calcDate);
                paramIndices[i].setParamIndexLevel(paramIndexLevel);
                paramIndices[i].setSourceDataDate(sourceDataDate);
            }
            index.setId(Integer.valueOf(indexId));
            index.setParamIndex(paramIndices);
            return index;
        }
        throw new IOException();
    }
}
