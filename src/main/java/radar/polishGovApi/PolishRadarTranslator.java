package radar.polishGovApi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data.MeasurementData;
import data.Sensor;
import data.Station;
import data.qualityIndex.AirQualityIndex;
import data.qualityIndex.ParamIndex;
import data.qualityIndex.ParamIndexLevel;
import radar.RadarTranslator;
import radar.Utils;

import java.io.IOException;

public class PolishRadarTranslator implements RadarTranslator {
    //these methods work typically with Polish Gov API

    @Override
    public AirQualityIndex readIndexData(String data) throws IOException {

        int paramsCount = Utils.getParamsCount();
        String[] params = Utils.getParams();

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
        throw new IOException("Air Quality Index Data could not be read.");
    }

    @Override
    public MeasurementData readMeasurementData(String sensorData) {
        return new Gson().fromJson(sensorData, MeasurementData.class);
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
