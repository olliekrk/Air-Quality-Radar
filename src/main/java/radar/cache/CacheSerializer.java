package radar.cache;

import data.MeasurementData;
import data.Sensor;
import data.Station;
import data.qualityIndex.AirQualityIndex;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class CacheSerializer implements Serializable {

    private LocalDateTime updateDate;
    private Map<String, Station> allStations;
    private Map<Integer, List<Sensor>> allSensors;
    private Map<Integer, MeasurementData> allData;
    private Map<Integer, AirQualityIndex> allIndices;

    CacheSerializer(LocalDateTime updateDate, Map<String, Station> allStations, Map<Integer, List<Sensor>> allSensors, Map<Integer, MeasurementData> allData, Map<Integer, AirQualityIndex> allIndices) {
        this.updateDate = updateDate;
        this.allStations = allStations;
        this.allSensors = allSensors;
        this.allData = allData;
        this.allIndices = allIndices;
    }

    LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public Map<String, Station> getAllStations() {
        return allStations;
    }

    public Map<Integer, List<Sensor>> getAllSensors() {
        return allSensors;
    }

    public Map<Integer, MeasurementData> getAllData() {
        return allData;
    }

    public Map<Integer, AirQualityIndex> getAllIndices() {
        return allIndices;
    }
}
