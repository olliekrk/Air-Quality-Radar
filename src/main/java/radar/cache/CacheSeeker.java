package radar.cache;

import data.MeasurementData;
import data.ParamType;
import data.Sensor;
import data.Station;
import data.AirQualityIndex;

import java.util.List;

public class CacheSeeker {

    private Cache cache;

    public CacheSeeker(Cache cache) {
        this.cache = cache;
    }

    public Sensor findStationSensorParam(Integer stationId, ParamType param) throws MissingDataException {
        List<Sensor> stationSensors = findStationSensors(stationId);
        for (Sensor sensor : stationSensors) {
            if (sensor.getParam().getParamFormula().equals(param.getParamFormula()))
                return sensor;
        }
        throw new MissingDataException();
    }

    public Cache getCache() {
        return cache;
    }

    public Station findStation(String stationName) throws MissingDataException {
        Station station = cache.getAllStations().get(stationName);
        if (station != null) return station;
        throw new MissingDataException();
    }

    public List<Sensor> findStationSensors(Integer stationId) throws MissingDataException {
        List<Sensor> sensors = cache.getAllSensors().get(stationId);
        if (sensors != null && !sensors.isEmpty()) return sensors;
        throw new MissingDataException();
    }

    public AirQualityIndex findIndex(Integer stationId) throws MissingDataException {
        AirQualityIndex index = cache.getAllIndices().get(stationId);
        if (index != null) return index;
        throw new MissingDataException();
    }

    public MeasurementData findData(Integer sensorId) throws MissingDataException {
        MeasurementData data = cache.getAllData().get(sensorId);
        if (data != null) return data;
        throw new MissingDataException();
    }
}
