package radar;

import data.MeasurementData;
import data.Sensor;
import data.Station;
import data.AirQualityIndex;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class Cache implements Serializable {

    private LocalDateTime updateDate;
    private Map<String, Station> allStations;
    private Map<Integer, List<Sensor>> allSensors;
    private Map<Integer, MeasurementData> allData;
    private Map<Integer, AirQualityIndex> allIndices;

    Cache(LocalDateTime updateDate, Map<String, Station> allStations, Map<Integer, List<Sensor>> allSensors, Map<Integer, MeasurementData> allData, Map<Integer, AirQualityIndex> allIndices) {
        this.updateDate = updateDate;
        this.allStations = allStations;
        this.allSensors = allSensors;
        this.allData = allData;
        this.allIndices = allIndices;
    }

    LocalDateTime getUpdateDate() {
        return updateDate;
    }

    Map<String, Station> getAllStations() {
        return allStations;
    }

    Map<Integer, List<Sensor>> getAllSensors() {
        return allSensors;
    }

    Map<Integer, MeasurementData> getAllData() {
        return allData;
    }

    Map<Integer, AirQualityIndex> getAllIndices() {
        return allIndices;
    }
}
