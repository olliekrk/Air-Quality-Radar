package radar;

import data.*;
import exceptions.MissingDataException;

import java.util.List;

public class CacheSeeker {

    private Cache cache;

    public CacheSeeker(Cache cache) {
        this.cache = cache;
    }

    Cache getCache() {
        return cache;
    }

    MeasurementData findData(Integer sensorId) throws MissingDataException {
        MeasurementData data = cache.getAllData().get(sensorId);
        if (data != null) return data;
        throw new MissingDataException("Failed to find data of sensor with id " + sensorId);
    }

    AirQualityIndex findIndex(Integer stationId) throws MissingDataException {
        AirQualityIndex index = cache.getAllIndices().get(stationId);
        if (index != null) return index;
        throw new MissingDataException("Failed to find air quality index on station with id " + stationId);
    }

    Station findStation(String stationName) throws MissingDataException {
        Station station = cache.getAllStations().get(stationName);
        if (station != null) return station;
        throw new MissingDataException("Failed to find station named " + stationName);
    }

    List<Sensor> findStationSensors(Integer stationId) throws MissingDataException {
        List<Sensor> sensors = cache.getAllSensors().get(stationId);
        if (sensors != null && !sensors.isEmpty()) return sensors;
        throw new MissingDataException("Failed to find any sensor on station with id " + stationId);
    }

    Sensor findStationSensorParam(Integer stationId, ParamType param) throws MissingDataException {
        List<Sensor> stationSensors = findStationSensors(stationId);
        for (Sensor sensor : stationSensors) {
            if (sensor.getParam().getParamFormula().compareToIgnoreCase(param.getParamFormula()) == 0)
                return sensor;
        }
        throw new MissingDataException("Failed to find sensor of " + param.getParamFormula() + " on station with id: " + stationId);
    }
}
