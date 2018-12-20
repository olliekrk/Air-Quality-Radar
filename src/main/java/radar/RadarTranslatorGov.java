package radar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data.*;

import java.io.IOException;

/**
 * Class implementing {@link RadarTranslator} interface, which methods
 * work typically with Polish Gov API.
 */
public class RadarTranslatorGov implements RadarTranslator {

    @Override
    public AirQualityIndex readIndexData(String data) throws IOException {

        int paramsCount = ParamType.getParamCount();
        String[] paramCodes = ParamType.getAllParamCodes();

        AirQualityIndex index = new AirQualityIndex();

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(data);
        if (jsonTree.isJsonObject()) {

            JsonObject indexObject = jsonTree.getAsJsonObject();
            String indexId = indexObject.get("id").getAsString();

            ParamIndex[] paramIndices = new ParamIndex[paramsCount];
            for (int i = 0; i < paramsCount; i++) {
                paramIndices[i] = new ParamIndex();

                JsonElement calcDateEl = indexObject.get(paramCodes[i].toLowerCase().concat("CalcDate"));
                JsonElement sourceDataDateEl = indexObject.get(paramCodes[i].toLowerCase().concat("SourceDataDate"));
                JsonElement indexElement = indexObject.get(paramCodes[i].toLowerCase().concat("IndexLevel"));

                String calcDate = calcDateEl.isJsonNull() ? null : calcDateEl.getAsString();
                String sourceDataDate = sourceDataDateEl.isJsonNull() ? null : sourceDataDateEl.getAsString();
                ParamIndexLevel paramIndexLevel = null;

                if (!indexElement.isJsonNull()) {
                    JsonObject indexLevel = indexElement.getAsJsonObject();
                    String indexLevelId = indexLevel.get("id").getAsString();
                    String indexLevelName = indexLevel.get("indexLevelName").getAsString();
                    paramIndexLevel = new ParamIndexLevel(indexLevelId, indexLevelName);
                }
                paramIndices[i].setParamName(paramCodes[i]);
                paramIndices[i].setCalcDate(calcDate);
                paramIndices[i].setParamIndexLevel(paramIndexLevel);
                paramIndices[i].setSourceDataDate(sourceDataDate);
            }
            index.setId(Integer.valueOf(indexId));
            index.setParamIndex(paramIndices);
            return index;
        }
        throw new IOException("Air Quality Index Data could not be read.");
    }

    @Override
    public MeasurementData readMeasurementData(String measurementData) {
        return new Gson().fromJson(measurementData, MeasurementData.class);
    }

    @Override
    public Sensor[] readSensorsData(String sensorsData) {
        return new Gson().fromJson(sensorsData, Sensor[].class);
    }

    @Override
    public Station[] readStationsData(String stationsData) {
        return new Gson().fromJson(stationsData, Station[].class);
    }
}
