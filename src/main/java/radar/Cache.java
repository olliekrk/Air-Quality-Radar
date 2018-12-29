package radar;

import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Serializable class used to store all extracted data. Used both to extract
 * date from local JSON cache file and to save extracted data for further use.
 */
class Cache implements Serializable {

    /**
     * Date and time of last cache file update.
     */
    private LocalDateTime updateDate;
    /**
     * Map of all stations, keys are stations' names.
     */
    private Map<String, Station> allStations;
    /**
     * Map of all stations' sensors, keys are stations' ids.
     */
    private Map<Integer, List<Sensor>> allSensors;
    /**
     * Map of all sensor measurement data, keys are sensors' ids.
     */
    private Map<Integer, MeasurementData> allData;
    /**
     * Map of all air quality indices, keys are stations' ids.
     */
    private Map<Integer, AirQualityIndex> allIndices;

    Cache(LocalDateTime updateDate, Map<String, Station> allStations, Map<Integer, List<Sensor>> allSensors, Map<Integer, MeasurementData> allData, Map<Integer, AirQualityIndex> allIndices) {
        this.updateDate = updateDate;
        this.allStations = allStations;
        this.allSensors = allSensors;
        this.allData = allData;
        this.allIndices = allIndices;
    }

    /* getters */

    LocalDateTime getUpdateDate() {
        return updateDate;
    }

    Map<Integer, MeasurementData> getAllData() {
        return allData;
    }

    Map<Integer, AirQualityIndex> getAllIndices() {
        return allIndices;
    }

    Map<Integer, List<Sensor>> getAllSensors() {
        return allSensors;
    }

    Map<String, Station> getAllStations() {
        return allStations;
    }
}
